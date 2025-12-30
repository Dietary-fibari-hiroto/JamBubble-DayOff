package com.example.jambubble_client.spotifyremote.data.repository

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.jambubble_client.data.model.PlaybackStatus
import com.example.jambubble_client.spotifyremote.data.model.PlayerState
import com.example.jambubble_client.spotifyremote.data.model.QueueState
import com.example.jambubble_client.spotifyremote.data.model.QueueTrack
import com.example.jambubble_client.spotifyremote.service.SpotifyRemoteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/*
* Spotify操作の窓口みたいなもん
* SpotifyRemoteServiceへのアクセス
* 複数の画面でプレイヤー状態を共有
* まぁUIとServiceの橋渡しやね
* */
class SpotifyRepository(
    private val application: Application,
    // 状態更新のコールバック
    private val onTrackStatusChanged: ((itemId: String, status: PlaybackStatus) -> Unit)? = null
) {

    companion object {
        private const val TAG = "SpotifyRepository"
    }

    // Repository専用のCoroutineScope
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Serviceリファレンス
    private var service: SpotifyRemoteService? = null

    // Serviceバインディング状態
    private val _isServiceBound = MutableStateFlow(false)
    val isServiceBound: StateFlow<Boolean> = _isServiceBound.asStateFlow()

    // Spotify接続状態(Serviceから転送)
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    // プレイヤー状態
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    // キュー状態
    private val _queueState = MutableStateFlow(QueueState())
    val queueState: StateFlow<QueueState> = _queueState.asStateFlow()

    // エラーメッセージ
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    //現在再生中のトラックID
    private var currentPlayingTrackId: String? = null

    // Serviceが接続したときのコールバック
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d(TAG, "サービスに接続しました。")
            val localBinder = binder as SpotifyRemoteService.LocalBinder
            service = localBinder.getService()
            _isServiceBound.value = true

            // Serviceの状態をRepositoryに転送開始
            observeServiceStates()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "サービスから切断されました。")
            service = null
            _isServiceBound.value = false
            _isConnected.value = false
        }
    }

    // Serviceの状態を監視してRepositoryに反映
    private fun observeServiceStates() {
        service?.let { svc ->
            // 接続状態の監視
            repositoryScope.launch {
                svc.isConnected.collect { connected ->
                    Log.d(TAG, "接続状態が変更されました: $connected")
                    _isConnected.value = connected

                    if (!connected && _isServiceBound.value) {
                        delay(2000) // 2秒待って再接続
                        svc.connect()
                    }
                }
            }

            // プレイヤー状態の監視
            repositoryScope.launch {
                svc.playerState.collect { state ->
                    Log.d(TAG, "プレイヤー状態が変更されました: ${state.displayInfo}")
                    _playerState.value = state
                }
            }

            //キュー状態の監視（トラック変更を検出）
            repositoryScope.launch {
                svc.queueState.collect { state ->
                    Log.d(TAG, "キュー状態が変更されました: ${state.tracks.size}曲, 現在: ${state.currentIndex}")

                    // 前回のキュー状態と比較
                    val previousQueue = _queueState.value
                    _queueState.value = state

                    // トラックが変更された場合
                    if (state.currentTrack != null &&
                        state.currentTrack?.id != previousQueue.currentTrack?.id) {

                        // 前の曲を「再生済み」にする
                        if (previousQueue.currentTrack != null) {
                            val completedTrackId = previousQueue.currentTrack!!.id
                            Log.d(TAG, "曲が完了しました: $completedTrackId")
                            notifyTrackCompleted(completedTrackId)
                        }

                        // 新しい曲を「再生中」にする
                        val nowPlayingTrackId = state.currentTrack!!.id
                        currentPlayingTrackId = nowPlayingTrackId
                        Log.d(TAG, "曲の再生を開始: $nowPlayingTrackId")
                        notifyTrackPlaying(nowPlayingTrackId)
                    }
                }
            }

            // エラーメッセージの監視
            repositoryScope.launch {
                svc.errorMessage.collect { error ->
                    _errorMessage.value = error
                    if (error != null) {
                        Log.e(TAG, "エラー: $error")
                    }
                }
            }
        }
    }

    //曲が再生中になったことを通知
    private fun notifyTrackPlaying(trackId: String) {
        onTrackStatusChanged?.invoke(trackId, PlaybackStatus.PLAYING)
    }

    //曲が完了したことを通知
    private fun notifyTrackCompleted(trackId: String) {
        onTrackStatusChanged?.invoke(trackId, PlaybackStatus.COMPLETED)
    }

    // Serviceにバインド
    fun bindService() {
        if (_isServiceBound.value) {
            Log.d(TAG, "サービスは既にバインド済みです。")
            return
        }

        Log.d(TAG, "サービスにバインドします。")
        val intent = Intent(application, SpotifyRemoteService::class.java)
        val bound = application.bindService(
            intent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        if (bound) {
            Log.d(TAG, "バインドリクエスト成功")
        } else {
            Log.e(TAG, "バインド失敗")
        }
    }

    // Serviceからアンバインド
    fun unbindService() {
        if (_isServiceBound.value) {
            application.unbindService(serviceConnection)
            _isServiceBound.value = false
            service = null
            Log.d(TAG, "サービスからアンバインドしました。")
        }
    }

    // Repositoryのクリーンアップ
    fun cleanup() {
        repositoryScope.cancel()
        unbindService()
    }

    // Spotifyに接続
    fun connect() {
        if (!_isServiceBound.value) {
            Log.e(TAG, "接続できません: サービスがバインドされていません")
            _errorMessage.value = "Service not ready"
            return
        }
        Log.d(TAG, "Spotify接続を開始します。")
        service?.connect()
    }

    // Spotifyから切断
    fun disconnect() {
        Log.d(TAG, "Spotifyから切断します。")
        service?.disconnect()
    }

    // ============ キュー管理操作 ============

    /**
     * キューに楽曲を追加
     */
    fun addToQueue(track: QueueTrack) {
        checkServiceAndExecute("addToQueue") { it.addToQueue(track) }
    }

    /**
     * キューに複数の楽曲を追加
     */
    fun addAllToQueue(tracks: List<QueueTrack>) {
        checkServiceAndExecute("addAllToQueue") { it.addAllToQueue(tracks) }
    }

    /**
     * キューをクリア
     */
    fun clearQueue() {
        checkServiceAndExecute("clearQueue") { it.clearQueue() }
    }

    /**
     * キューから楽曲を削除
     */
    fun removeFromQueue(trackId: String) {
        checkServiceAndExecute("removeFromQueue") { it.removeFromQueue(trackId) }
    }

    /**
     * キューの並び替え
     */
    fun reorderQueue(orderedTrackIds: List<String>) {
        checkServiceAndExecute("reorderQueue") { it.reorderQueue(orderedTrackIds) }
    }

    /**
     * 特定のインデックスから再生
     */
    fun playQueueFromIndex(index: Int) {
        checkServiceAndExecute("playQueueFromIndex") { it.playQueueFromIndex(index) }
    }

    /**
     * キュー内の次の曲を再生
     */
    fun playNextInQueue() {
        checkServiceAndExecute("playNextInQueue") { it.playNextInQueue() }
    }

    /**
     * キュー内の前の曲を再生
     */
    fun playPreviousInQueue() {
        checkServiceAndExecute("playPreviousInQueue") { it.playPreviousInQueue() }
    }

    // ============ 既存の再生コントロール操作 ============

    fun togglePlayPause() {
        checkServiceAndExecute("togglePlayPause") { it.togglePlayPause() }
    }

    fun play() {
        checkServiceAndExecute("play") { it.play() }
    }

    fun pause() {
        checkServiceAndExecute("pause") { it.pause() }
    }

    fun skipNext() {
        checkServiceAndExecute("skipNext") { it.skipNext() }
    }

    fun skipPrevious() {
        checkServiceAndExecute("skipPrevious") { it.skipPrevious() }
    }

    fun playTrack(uri: String) {
        checkServiceAndExecute("playTrack") { it.playTrack(uri) }
    }

    fun toggleShuffle() {
        checkServiceAndExecute("toggleShuffle") { it.toggleShuffle() }
    }

    fun toggleRepeat() {
        checkServiceAndExecute("toggleRepeat") { it.toggleRepeat() }
    }

    fun seekTo(positionMs: Long) {
        checkServiceAndExecute("seekTo") { it.seekTo(positionMs) }
    }

    // Serviceが利用可能かチェックして実行
    private fun checkServiceAndExecute(
        operation: String,
        action: (SpotifyRemoteService) -> Unit
    ) {
        service?.let { svc ->
            Log.d(TAG, "操作実行: $operation")
            action(svc)
        } ?: run {
            Log.e(TAG, "$operation を実行できません: サービスがバインドされていません")
            _errorMessage.value = "Service not available"
        }
    }
}