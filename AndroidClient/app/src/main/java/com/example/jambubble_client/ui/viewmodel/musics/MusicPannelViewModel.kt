package com.example.jambubble_client.ui.viewmodel.musics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.spotifyremote.data.model.PlayerState
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MusicPannelModelFactory(
    private val repository: SpotifyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicPannelViewModel::class.java)) {
            return MusicPannelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class MusicPannelViewModel(
    private val repository: SpotifyRepository
): ViewModel(){
    //UI状態
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init{
        //Serviceにバインド
        repository.bindService()

        //Repositoryの状態を監視
        observeRepositoryStates()
    }

    //Repositoryの状態をUI状態に反映
    private fun observeRepositoryStates(){
        viewModelScope.launch{
            //ServiceBinding状態
            repository.isServiceBound.collect{isBound->
                _uiState.value = _uiState.value.copy(isServiceBound = isBound)
            }
        }

        viewModelScope.launch{
            //Spotify接続状態
            repository.isConnected.collect{isConnected ->
                _uiState.value = _uiState.value.copy(isConnected = isConnected)
            }
        }

        viewModelScope.launch{
            //プレイヤー状態
            repository.playerState.collect{playerState ->
                _uiState.value = _uiState.value.copy(playerState = playerState)
            }
        }

        viewModelScope.launch{
            //エラーメッセージ
            repository.errorMessage.collect{error->
                _uiState.value = _uiState.value.copy(errorMessage = error)

            }
        }
    }

//Spotify接続操作
    fun connectToSpotify() {
        repository.connect()
    }

    fun disconnectFromSpotify() {
        repository.disconnect()
    }

    //再生コントロール操作

    fun togglePlayPause() {
        repository.togglePlayPause()
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

    fun toggleShuffle() {
        repository.toggleShuffle()
    }

    fun toggleRepeat() {
        repository.toggleRepeat()
    }

    fun seekTo(positionMs: Long) {
        repository.seekTo(positionMs)
    }



    //えらー処理
    fun clearError(){
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    override fun onCleared(){
        super.onCleared()
        //VIewModelが破棄されるときはServiceからアンバインド
        repository.unbindService()
    }

    data class UiState(
        val isServiceBound: Boolean = false,
        val isConnected: Boolean = false,
        val playerState: PlayerState = PlayerState(),
        val errorMessage: String? = null
    ) {
        val connectionStatus: String
            get() = when {
                !isServiceBound -> "サービスにバインドしてないです"
                !isConnected -> "切断"
                else -> "接続成功 🎉"
            }

        val canControl: Boolean
            get() = isServiceBound && isConnected
    }

}

