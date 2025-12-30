package com.example.jambubble_client.spotifyremote.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.jambubble_client.Config
import com.example.jambubble_client.spotifyremote.data.model.PlayerState
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.jambubble_client.R
import com.example.jambubble_client.spotifyremote.data.model.QueueState
import com.example.jambubble_client.spotifyremote.data.model.QueueTrack


/*
* SpotifyAppRemote接続を管理するバックグラウンドService
*
* ここではアプリとの接続を維持したり再生状態の管理とかコントロール操作までやる
* */

class SpotifyRemoteService : Service() {
    companion object {
        private const val TAG = "SpotifyRemoteService"
        private const val CLIENT_ID = Config.SPOTIFY_CLIENT_ID
        private const val REDIRECT_URL = Config.SPOTIFY_REDIRECT_URL
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "spotify_service_channel"

        // 曲の終わり判定のしきい値（曲の残り時間がこれ以下なら次へ）
        private const val TRACK_END_THRESHOLD_MS = 2000L
    }

    private var reconnectJob: Job? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // 曲の終わりを監視するJob
    private var trackMonitorJob: Job? = null

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): SpotifyRemoteService = this@SpotifyRemoteService
    }

    // SpotifyAppRemoteのインスタンス
    private var spotifyAppRemote: SpotifyAppRemote? = null

    // 接続状態
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    // プレイヤー状態
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    // キュー状態
    private val _queueState = MutableStateFlow(QueueState())
    val queueState: StateFlow<QueueState> = _queueState.asStateFlow()

    // エラーの状態
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "サービスバウンド")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "サービス生成")
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Spotify接続",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Spotifyとの接続を維持します"
                setShowBadge(false)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        reconnectJob?.cancel()
        trackMonitorJob?.cancel()
        serviceScope.cancel()
        disconnect()
        Log.d(TAG, "サービス破棄")
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Spotify接続中")
            .setContentText("音楽を再生できます")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
    // 接続処理
    fun connect() {
        if (spotifyAppRemote != null) {
            Log.d(TAG, "すでにコネクトしてまっせ")
            return
        }

        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URL)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(
            this,
            connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(remote: SpotifyAppRemote) {
                    spotifyAppRemote = remote
                    _isConnected.value = true
                    _errorMessage.value = null
                    Log.d(TAG, "✅ Spotifyに接続しました！")

                    subscribeToPlayerState()
                    Log.d(TAG, "プレイヤー状態の監視を開始")

                    startTrackMonitoring()
                    Log.d(TAG, "曲の終わり監視を開始")
                }

                override fun onFailure(throwable: Throwable) {
                    _isConnected.value = false
                    _errorMessage.value = throwable.message ?: "Connection failed"
                    Log.e(TAG, "❌ 接続失敗", throwable)
                }
            }
        )
    }

    // Spotifyアプリから切断
    fun disconnect() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
            spotifyAppRemote = null
        }
        trackMonitorJob?.cancel()
        _isConnected.value = false
        _playerState.value = PlayerState()
        Log.d(TAG, "コネクト解除しました")
    }

    // プレイヤー状態の監視を開始
    private fun subscribeToPlayerState() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback { state ->
            updatePlayerState(state)
        }?.setErrorCallback { error ->
            Log.e(TAG, "Player state error", error)
            handleDisconnection()
        }
    }
    // プレイヤー状態を更新する共通関数
    private fun updatePlayerState(state: com.spotify.protocol.types.PlayerState) {
        val track = state.track

        _playerState.value = PlayerState(
            trackName = track?.name ?: "",
            artistName = track?.artist?.name ?: "",
            albumName = track?.album?.name ?: "",
            imageUri = track?.imageUri?.raw ?: "",
            isPaused = state.isPaused,
            playbackPosition = state.playbackPosition,
            duration = track?.duration ?: 0L,
            isShuffling = state.playbackOptions.isShuffling,
            repeatMode = when (state.playbackOptions.repeatMode) {
                0 -> PlayerState.RepeatMode.OFF
                1 -> PlayerState.RepeatMode.TRACK
                2 -> PlayerState.RepeatMode.CONTEXT
                else -> PlayerState.RepeatMode.OFF
            }
        )

        // キュー状態の再生フラグを更新
        _queueState.value = _queueState.value.copy(isPlaying = !state.isPaused)

        // デバッグログ
        Log.d(TAG, "プレイヤーの状態を更新: ${_playerState.value.displayInfo} " +
                "(${state.playbackPosition}/${track?.duration ?: 0}ms, " +
                "isPaused=${state.isPaused}, " +
                "キューサイズ=${_queueState.value.tracks.size}, " +
                "現在インデックス=${_queueState.value.currentIndex})")
    }

    // 曲の終わりを監視して自動的に次の曲へ
    private fun startTrackMonitoring() {
        Log.d(TAG, "=== 曲監視開始 ===")

        trackMonitorJob?.cancel()
        trackMonitorJob = serviceScope.launch {
            Log.d(TAG, "曲監視Jobが起動しました")

            var loopCount = 0
            while (isActive) {
                delay(1000) // 1秒ごとにチェック
                loopCount++

                // 🔥 重要: 毎秒プレイヤー状態を取得（ポーリング）
                try {
                    spotifyAppRemote?.playerApi?.playerState?.setResultCallback { state ->
                        // コールバック内でStateFlowを更新
                        serviceScope.launch {
                            updatePlayerState(state)

                            val currentState = _playerState.value
                            val queueState = _queueState.value

                            // 10秒ごとに状態をログ出力
                            if (loopCount % 10 == 0) {
                                Log.d(TAG, "監視中 [${loopCount}秒]: " +
                                        "isPaused=${currentState.isPaused}, " +
                                        "duration=${currentState.duration}, " +
                                        "position=${currentState.playbackPosition}, " +
                                        "hasNext=${queueState.hasNext}, " +
                                        "キューサイズ=${queueState.tracks.size}")
                            }

                            // 曲が再生中で、キューに次の曲があり、曲の終わりが近い場合
                            if (!currentState.isPaused &&
                                queueState.hasNext &&
                                currentState.duration > 0 &&
                                currentState.playbackPosition > 0) {

                                val remainingTime = currentState.duration - currentState.playbackPosition

                                // 残り時間を定期的にログ
                                if (loopCount % 10 == 0) {
                                    Log.d(TAG, "残り時間: ${remainingTime}ms (しきい値: ${TRACK_END_THRESHOLD_MS}ms)")
                                }

                                if (remainingTime <= TRACK_END_THRESHOLD_MS && remainingTime > 0) {
                                    Log.d(TAG, "🎵 曲の終わりを検出（残り${remainingTime}ms）。次の曲へ移動します。")
                                    playNextInQueue()

                                    // 次の曲に移行したので少し待つ
                                    delay(3000)
                                    loopCount = 0 // カウンターリセット
                                }
                            }
                        }
                    }?.setErrorCallback { error ->
                        Log.e(TAG, "playerState取得エラー", error)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "監視ループエラー", e)
                }
            }

            Log.d(TAG, "曲監視Jobが終了しました")
        }
    }

    private fun handleDisconnection() {
        if (_isConnected.value) {
            Log.w(TAG, "接続が切断されました。再接続を試みます。")
            _isConnected.value = false
            spotifyAppRemote = null
            trackMonitorJob?.cancel()
            scheduleReconnect()
        }
    }

    private fun scheduleReconnect() {
        reconnectJob?.cancel()
        reconnectJob = serviceScope.launch {
            var retryCount = 0
            while (retryCount < 5 && spotifyAppRemote == null) {
                delay(3000L * (retryCount + 1))
                Log.d(TAG, "再接続試行 ${retryCount + 1}/5")
                connect()
                retryCount++
            }
        }
    }

    // ============ キュー管理機能 ============

    /**
     * キューに楽曲を追加
     */
    fun addToQueue(track: QueueTrack) {
        val currentQueue = _queueState.value
        val newTracks = currentQueue.tracks + track

        _queueState.value = currentQueue.copy(tracks = newTracks)

        Log.d(TAG, "キューに追加: ${track.trackName}, キューサイズ: ${newTracks.size}")

        // キューが空だった場合、すぐに再生開始
        if (currentQueue.isEmpty) {
            playQueueFromIndex(0)
        }
    }

    /**
     * キューに複数の楽曲を追加
     */
    fun addAllToQueue(tracks: List<QueueTrack>) {
        val currentQueue = _queueState.value
        val newTracks = currentQueue.tracks + tracks

        _queueState.value = currentQueue.copy(tracks = newTracks)

        Log.d(TAG, "${tracks.size}曲をキューに追加, キューサイズ: ${newTracks.size}")

        // キューが空だった場合、すぐに再生開始
        if (currentQueue.isEmpty && tracks.isNotEmpty()) {
            playQueueFromIndex(0)
        }
    }

    /**
     * キューをクリア
     */
    fun clearQueue() {
        _queueState.value = QueueState()
        pause()
        Log.d(TAG, "キューをクリアしました")
    }

    /**
     * キューから楽曲を削除
     */
    fun removeFromQueue(trackId: String) {
        val currentQueue = _queueState.value
        val currentIndex = currentQueue.currentIndex

        val indexToRemove = currentQueue.tracks.indexOfFirst { it.id == trackId }
        if (indexToRemove == -1) {
            Log.w(TAG, "削除対象の曲が見つかりません: $trackId")
            return
        }

        val newTracks = currentQueue.tracks.filterIndexed { index, _ -> index != indexToRemove }

        // 現在再生中の曲を削除した場合
        val newIndex = when {
            indexToRemove == currentIndex -> {
                // 現在の曲を削除 → 次の曲を再生
                if (newTracks.isNotEmpty()) {
                    playQueueFromIndex(currentIndex.coerceAtMost(newTracks.size - 1))
                }
                currentIndex.coerceAtMost(newTracks.size - 1)
            }
            indexToRemove < currentIndex -> currentIndex - 1 // 前の曲を削除 → インデックス調整
            else -> currentIndex // 後ろの曲を削除 → インデックスそのまま
        }

        _queueState.value = currentQueue.copy(
            tracks = newTracks,
            currentIndex = if (newTracks.isEmpty()) -1 else newIndex
        )

        Log.d(TAG, "キューから削除: $trackId, 新しいキューサイズ: ${newTracks.size}")
    }

    /**
     * キューの並び替え
     */
    fun reorderQueue(orderedTrackIds: List<String>) {
        val currentQueue = _queueState.value
        val currentTrackId = currentQueue.currentTrack?.id

        val newTracks = orderedTrackIds.mapNotNull { id ->
            currentQueue.tracks.find { it.id == id }
        }

        // 現在再生中の曲の新しいインデックスを見つける
        val newIndex = if (currentTrackId != null) {
            newTracks.indexOfFirst { it.id == currentTrackId }
        } else {
            -1
        }

        _queueState.value = currentQueue.copy(
            tracks = newTracks,
            currentIndex = newIndex
        )

        Log.d(TAG, "キューを並び替えました。新しいサイズ: ${newTracks.size}")
    }

    /**
     * 特定のインデックスから再生
     */
    fun playQueueFromIndex(index: Int) {
        val currentQueue = _queueState.value

        if (index < 0 || index >= currentQueue.tracks.size) {
            Log.w(TAG, "無効なインデックス: $index")
            return
        }

        val track = currentQueue.tracks[index]
        val spotifyUri = "spotify:track:${track.spotifyTrackId}"

        spotifyAppRemote?.playerApi?.play(spotifyUri)

        _queueState.value = currentQueue.copy(currentIndex = index)

        Log.d(TAG, "キューから再生: [$index] ${track.trackName}")
    }

    /**
     * キュー内の次の曲を再生
     */
    fun playNextInQueue() {
        val currentQueue = _queueState.value

        if (!currentQueue.hasNext) {
            Log.d(TAG, "キューに次の曲がありません")
            pause()
            return
        }

        playQueueFromIndex(currentQueue.currentIndex + 1)
    }

    /**
     * キュー内の前の曲を再生
     */
    fun playPreviousInQueue() {
        val currentQueue = _queueState.value

        if (!currentQueue.hasPrevious) {
            Log.d(TAG, "キューに前の曲がありません")
            return
        }

        playQueueFromIndex(currentQueue.currentIndex - 1)
    }

    // ============ 既存の再生コントロール ============

    fun togglePlayPause() {
        spotifyAppRemote?.let { remote ->
            if (_playerState.value.isPaused) {
                remote.playerApi.resume()
                Log.d(TAG, "再生")
            } else {
                remote.playerApi.pause()
                Log.d(TAG, "一時停止")
            }
        } ?: run {
            _errorMessage.value = "Spotifyアプリに接続されていません"
        }
    }

    fun play() {
        spotifyAppRemote?.playerApi?.resume()
        Log.d(TAG, "再生")
    }

    fun pause() {
        spotifyAppRemote?.playerApi?.pause()
        Log.d(TAG, "一時停止")
    }

    // 次の曲へ（キュー対応版）
    fun skipNext() {
        if (_queueState.value.tracks.isNotEmpty()) {
            playNextInQueue()
        } else {
            spotifyAppRemote?.playerApi?.skipNext()
            Log.d(TAG, "次の曲へ（通常）")
        }
    }

    // 前の曲へ（キュー対応版）
    fun skipPrevious() {
        if (_queueState.value.tracks.isNotEmpty()) {
            playPreviousInQueue()
        } else {
            spotifyAppRemote?.playerApi?.skipPrevious()
            Log.d(TAG, "前の曲へ（通常）")
        }
    }

    fun playTrack(uri: String) {
        spotifyAppRemote?.playerApi?.play(uri)
        Log.d(TAG, "特定のトラックを再生")
    }

    fun toggleShuffle() {
        spotifyAppRemote?.playerApi?.setShuffle(!_playerState.value.isShuffling)
        Log.d(TAG, "シャッフルモード切り替え:${!_playerState.value.isShuffling}")
    }

    fun toggleRepeat() {
        val nextMode = when (_playerState.value.repeatMode) {
            PlayerState.RepeatMode.OFF -> 2 //CONTEXT
            PlayerState.RepeatMode.CONTEXT -> 1 //TRACK
            PlayerState.RepeatMode.TRACK -> 0 //OFF
        }
        spotifyAppRemote?.playerApi?.setRepeat(nextMode)
    }

    fun seekTo(positionMs: Long) {
        spotifyAppRemote?.playerApi?.seekTo(positionMs)
        Log.d(TAG, "シーク位置へ移動:$positionMs ms")
    }
}