package com.example.jambubble_client.ui.viewmodel.musics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.data.model.AddTrackRequest
import com.example.jambubble_client.data.model.CreateSessionRequest
import com.example.jambubble_client.data.model.Guest
import com.example.jambubble_client.data.model.PlaylistItem
import com.example.jambubble_client.data.model.RemoveTrackRequest
import com.example.jambubble_client.data.model.ReorderPlaylistRequest
import com.example.jambubble_client.data.model.SpotifyTrackSearchResult
import com.example.jambubble_client.data.network.SignalRManager
import com.example.jambubble_client.spotifyremote.data.model.QueueTrack
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.map



/**
 * セッション管理とSpotify再生を統合したメインViewModel
 *
 * 責任:
 * - SignalRによるセッション管理
 * - プレイリストとキューの同期
 * - Spotify再生コントロールの統合
 */
class MusicSessionViewModelFactory(
    private val signalRManager: SignalRManager,
    private val spotifyRepository: SpotifyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicSessionViewModel::class.java)) {
            return MusicSessionViewModel(signalRManager, spotifyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

/**
 * セッション管理とSpotify再生を統合したメインViewModel
 *
 * 責任:
 * - SignalRによるセッション管理
 * - プレイリストとキューの同期
 * - Spotify再生コントロールの統合
 */
class MusicSessionViewModel(
    private val signalRManager: SignalRManager,
    private val spotifyRepository: SpotifyRepository
) : ViewModel() {

    companion object {
        private const val TAG = "MusicSessionViewModel"
        private const val HOST_USER_ID = "HOST_USER_ID"
    }

    // ========== SignalR関連の状態 ==========

    private val _sessionId = MutableStateFlow<String?>(null)
    val sessionId: StateFlow<String?> = _sessionId

    private val _guestUrl = MutableStateFlow<String?>(null)
    val guestUrl: StateFlow<String?> = _guestUrl

    private val _playlist = MutableStateFlow<List<PlaylistItem>>(emptyList())
    val playlist: StateFlow<List<PlaylistItem>> = _playlist

    private val _isSessionActive = MutableStateFlow(false)
    val isSessionActive: StateFlow<Boolean> = _isSessionActive


    val guests:StateFlow<List<Guest>> = signalRManager.guests



    // ========== Spotify関連の状態 ==========

    // SpotifyRepositoryから直接公開
    val isSpotifyConnected = spotifyRepository.isConnected
    val playerState = spotifyRepository.playerState
    val queueState = spotifyRepository.queueState
    val isServiceBound = spotifyRepository.isServiceBound

    // ========== 共通UI状態 ==========

    private val _searchResults = MutableStateFlow<List<SpotifyTrackSearchResult>>(emptyList())
    val searchResults: StateFlow<List<SpotifyTrackSearchResult>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        Log.d(TAG, "ViewModel初期化開始")

        // Spotifyサービスにバインド（すぐに必要）
        spotifyRepository.bindService()

        // 各種監視を開始（SignalR接続前でもOK）
        observePlaylistChanges()  // ← SignalR接続前でも購読開始
        observeSessionClosed()
        observeSpotifyConnection()
        observeSpotifyErrors()

        Log.d(TAG, "ViewModel初期化完了（SignalR接続は手動で行う必要があります）")
    }

    /**
     * ユーザーがボタンを押したときに呼び出す
     * SignalRサーバーに接続する
     */
    fun connectToServer() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "SignalRサーバー接続開始")
                signalRManager.connect()
                Log.d(TAG, "SignalRサーバー接続完了")
            } catch (e: Exception) {
                _errorMessage.value = "サーバーへの接続に失敗しました: ${e.message}"
                Log.e(TAG, "SignalR接続エラー", e)
            }
        }
    }

    // ========== SignalR監視 ==========

    /**
     * プレイリスト変更を監視
     *
     * 重要: この関数はinit内で呼ばれるが、SignalR接続前でも問題ない。
     * Flow.collect()は接続後にイベントが来たら自動的に処理される。
     */
    private fun observePlaylistChanges() {
        viewModelScope.launch {
            Log.d(TAG, "プレイリスト監視開始")
            signalRManager.playlist.collect { playlist ->
                _playlist.value = playlist
                Log.d(TAG, "=== プレイリスト更新: ${playlist.size}曲 ===")

                // プレイリストをSpotifyキューに同期
                syncPlaylistToQueue(playlist)
            }
        }
    }

    private fun observeSessionClosed() {
        viewModelScope.launch {
            Log.d(TAG, "セッション終了監視開始")
            signalRManager.sessionClosed.collect { closed ->
                if (closed) {
                    _isSessionActive.value = false
                    _errorMessage.value = "セッションが終了しました"

                    // Spotifyも停止
                    spotifyRepository.pause()
                    spotifyRepository.clearQueue()

                    Log.d(TAG, "セッション終了")
                }
            }
        }
    }

    private fun observeSpotifyConnection() {
        viewModelScope.launch {
            Log.d(TAG, "Spotify接続状態監視開始")
            spotifyRepository.isConnected.collect { isConnected ->
                Log.d(TAG, "Spotify接続状態: $isConnected")

                if (!isConnected) {
                    Log.w(TAG, "Spotifyとの接続が切断されました")
                }
            }
        }
    }

    private fun observeSpotifyErrors() {
        viewModelScope.launch {
            Log.d(TAG, "Spotifyエラー監視開始")
            spotifyRepository.errorMessage.collect { error ->
                if (error != null) {
                    _errorMessage.value = "Spotify: $error"
                    Log.e(TAG, "Spotifyエラー: $error")
                }
            }
        }
    }

    // ========== プレイリスト ⇄ キュー同期 ==========

    /**
     * SignalRプレイリストをSpotifyキューに同期
     * プレイリストが更新されるたびに自動的に呼ばれる
     */
    private fun syncPlaylistToQueue(playlistItems: List<PlaylistItem>) {
        viewModelScope.launch {
            Log.d(TAG, "=== キュー同期開始 ===")

            val queueTracks = playlistItems.map { item ->
                QueueTrack(
                    id = item.id,
                    spotifyTrackId = item.spotifyTrackId,
                    trackName = item.trackName,
                    artistName = item.artistName,
                    albumName = item.albumName,
                    imageUrl = item.albumImageUrl,
                    durationMs = item.durationMs.toLong(),
                    requestedBy = item.requestedBy
                )
            }

            // 現在のキュー状態を取得
            val currentQueue = queueState.value

            // キューが空で、プレイリストに曲がある場合
            if (currentQueue.isEmpty && queueTracks.isNotEmpty()) {
                Log.d(TAG, "キューが空なのでプレイリスト全体を追加: ${queueTracks.size}曲")
                spotifyRepository.addAllToQueue(queueTracks)
            }
            // キューに曲がある場合は差分のみ追加
            else if (queueTracks.isNotEmpty()) {
                val existingIds = currentQueue.tracks.map { it.id }.toSet()
                val newTracks = queueTracks.filter { it.id !in existingIds }

                if (newTracks.isNotEmpty()) {
                    Log.d(TAG, "新しい曲を${newTracks.size}曲追加")
                    newTracks.forEach { track ->
                        Log.d(TAG, "  追加: ${track.trackName} by ${track.artistName}")
                    }
                    spotifyRepository.addAllToQueue(newTracks)
                } else {
                    Log.d(TAG, "新しい曲はありません（既に全てキューに存在）")
                }
            }

            Log.d(TAG, "=== キュー同期完了: 合計${queueTracks.size}曲 ===")
        }
    }

    // ========== セッション操作 ==========

    fun createSession(deviceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val request = CreateSessionRequest(deviceId)
                val response = signalRManager.createSession(request)

                if (response != null) {
                    _sessionId.value = response.sessionId
                    _guestUrl.value = response.guestUrl
                    _isSessionActive.value = true

                    Log.d(TAG, "セッション作成: ${response.sessionId}")
                } else {
                    _errorMessage.value = "セッションの作成に失敗しました"
                }
            } catch (e: Exception) {
                _errorMessage.value = "エラー: ${e.message}"
                Log.e(TAG, "Create session error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun closeSession() {
        viewModelScope.launch {
            val currentSessionId = _sessionId.value ?: return@launch

            try {
                val success = signalRManager.closeSession(currentSessionId)
                if (success) {
                    _isSessionActive.value = false
                    _sessionId.value = null
                    _guestUrl.value = null
                    _playlist.value = emptyList()

                    // Spotifyも停止
                    spotifyRepository.pause()
                    spotifyRepository.clearQueue()

                    Log.d(TAG, "セッション終了")
                }
            } catch (e: Exception) {
                _errorMessage.value = "セッションの終了に失敗: ${e.message}"
                Log.e(TAG, "Close session error", e)
            }
        }
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }

    // ========== 楽曲追加/削除 ==========

    fun addTrack(track: SpotifyTrackSearchResult) {
        viewModelScope.launch {
            val currentSessionId = _sessionId.value ?: return@launch

            try {
                val request = AddTrackRequest(
                    sessionId = currentSessionId,
                    spotifyTrackId = track.id,
                    trackName = track.name,
                    artistName = track.artist,
                    albumName = track.album,
                    albumImageUrl = track.albumImageUrl,
                    durationMs = track.durationMs,
                    requestedBy = "Host",
                    requestedByUserId = HOST_USER_ID
                )

                val success = signalRManager.addTrack(request)
                if (!success) {
                    _errorMessage.value = "曲の追加に失敗しました"
                } else {
                    Log.d(TAG, "曲を追加リクエスト: ${track.name}")
                }

                // SignalRで自動的にプレイリスト更新され、
                // observePlaylistChanges()でキューにも反映される

            } catch (e: Exception) {
                _errorMessage.value = "エラー: ${e.message}"
                Log.e(TAG, "Add track error", e)
            }
        }
    }

    fun removeTrack(itemId: String) {
        viewModelScope.launch {
            val currentSessionId = _sessionId.value ?: return@launch

            try {
                val request = RemoveTrackRequest(
                    sessionId = currentSessionId,
                    itemId = itemId,
                    requestedByUserId = HOST_USER_ID
                )

                val success = signalRManager.removeTrack(request)
                if (success) {
                    // SignalRでプレイリスト更新され、自動的にキューからも削除される
                    spotifyRepository.removeFromQueue(itemId)
                    Log.d(TAG, "曲を削除: $itemId")
                } else {
                    _errorMessage.value = "曲の削除に失敗しました"
                }
            } catch (e: Exception) {
                _errorMessage.value = "エラー: ${e.message}"
                Log.e(TAG, "Remove track error", e)
            }
        }
    }

    fun reorderPlaylist(orderedIds: List<String>) {
        viewModelScope.launch {
            val currentSessionId = _sessionId.value ?: return@launch

            try {
                val request = ReorderPlaylistRequest(
                    sessionId = currentSessionId,
                    orderedItemIds = orderedIds
                )

                val success = signalRManager.reorderPlaylist(request)
                if (success) {
                    // SignalRでプレイリスト更新され、自動的にキューも並び替えられる
                    spotifyRepository.reorderQueue(orderedIds)
                    Log.d(TAG, "プレイリストを並び替え")
                } else {
                    _errorMessage.value = "並び替えに失敗しました"
                }
            } catch (e: Exception) {
                _errorMessage.value = "エラー: ${e.message}"
                Log.e(TAG, "Reorder error", e)
            }
        }
    }

    // ========== Spotify操作 (Repository委譲) ==========

    fun connectToSpotify() {
        spotifyRepository.connect()
    }

    fun disconnectFromSpotify() {
        spotifyRepository.disconnect()
    }

    fun togglePlayPause() {
        spotifyRepository.togglePlayPause()
    }

    fun play() {
        spotifyRepository.play()
    }

    fun pause() {
        spotifyRepository.pause()
    }

    fun skipNext() {
        spotifyRepository.skipNext()
    }

    fun skipPrevious() {
        spotifyRepository.skipPrevious()
    }

    fun playTrackFromQueue(index: Int) {
        spotifyRepository.playQueueFromIndex(index)
    }

    fun toggleShuffle() {
        spotifyRepository.toggleShuffle()
    }

    fun toggleRepeat() {
        spotifyRepository.toggleRepeat()
    }

    fun seekTo(positionMs: Long) {
        spotifyRepository.seekTo(positionMs)
    }

    // ========== エラー処理 ==========

    fun clearError() {
        _errorMessage.value = null
    }

    // ========== クリーンアップ ==========

    override fun onCleared() {
        super.onCleared()
        signalRManager.disconnect()
        spotifyRepository.unbindService()
        Log.d(TAG, "ViewModel cleared")
    }
}