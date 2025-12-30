package com.example.jambubble_client.ui.viewmodel.musics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.spotifyremote.data.model.PlayerState
import com.example.jambubble_client.spotifyremote.data.model.QueueState
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.jvm.java


class MusicPannelViewModelFactory(
    private val repository: SpotifyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicPannelViewModel::class.java)) {
            return MusicPannelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

/**
 * セッション外でのSpotify操作専用ViewModel
 *
 * 責任:
 * - Spotify接続管理
 * - 再生コントロール
 * - キュー表示（読み取り専用）
 *
 * 使用例:
 * - セッションに参加していない時のSpotify操作
 * - 設定画面からのSpotifyテスト
 * - スタンドアロンの音楽プレーヤーとして
 */
class MusicPannelViewModel(
    private val repository: SpotifyRepository
) : ViewModel() {

    // UI状態をシンプルに管理
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Repositoryの状態を直接公開
    val isServiceBound = repository.isServiceBound
    val isConnected = repository.isConnected
    val playerState = repository.playerState
    val queueState = repository.queueState
    val errorMessage = repository.errorMessage

    init {
        // Serviceにバインド
        repository.bindService()

        // 状態を監視してUI状態に反映
        observeRepositoryStates()
    }

    private fun observeRepositoryStates() {
        viewModelScope.launch {
            repository.isServiceBound.collect { isBound ->
                _uiState.value = _uiState.value.copy(isServiceBound = isBound)
            }
        }

        viewModelScope.launch {
            repository.isConnected.collect { isConnected ->
                _uiState.value = _uiState.value.copy(isConnected = isConnected)
            }
        }

        viewModelScope.launch {
            repository.playerState.collect { playerState ->
                _uiState.value = _uiState.value.copy(playerState = playerState)
            }
        }

        viewModelScope.launch {
            repository.queueState.collect { queueState ->
                _uiState.value = _uiState.value.copy(queueState = queueState)
            }
        }
    }

    // ========== Spotify接続操作 ==========

    fun connectToSpotify() {
        repository.connect()
    }

    fun disconnectFromSpotify() {
        repository.disconnect()
    }

    // ========== 再生コントロール操作 ==========

    fun togglePlayPause() {
        repository.togglePlayPause()
    }

    fun play() {
        repository.play()
    }

    fun pause() {
        repository.pause()
    }

    fun skipNext() {
        repository.skipNext()
    }

    fun skipPrevious() {
        repository.skipPrevious()
    }

    fun playTrack(uri: String) {
        repository.playTrack(uri)
    }

    fun playTrackFromQueue(index: Int) {
        repository.playQueueFromIndex(index)
    }

    fun toggleShuffle() {
        repository.toggleShuffle()
    }

    fun toggleRepeat() {
        repository.toggleRepeat()
    }

    fun seekTo(positionMs: Long) {
        repository.seekTo(positionMs)
    }

    // ========== クリーンアップ ==========

    override fun onCleared() {
        super.onCleared()
        // ViewModelが破棄されるときはServiceからアンバインド
        repository.unbindService()
    }

    // ========== UI状態 ==========

    data class UiState(
        val isServiceBound: Boolean = false,
        val isConnected: Boolean = false,
        val playerState: PlayerState = PlayerState(),
        val queueState: QueueState = QueueState()
    ) {
        val connectionStatus: String
            get() = when {
                !isServiceBound -> "サービスにバインドしていません"
                !isConnected -> "切断"
                else -> "接続成功 🎉"
            }

        val canControl: Boolean
            get() = isServiceBound && isConnected
    }
}