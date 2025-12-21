package com.example.jambubble_client.ui.viewmodel.musics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.data.api.ApiConfig
import com.example.jambubble_client.data.model.AddTrackRequest
import com.example.jambubble_client.data.model.CreateSessionRequest
import com.example.jambubble_client.data.model.PlaylistItem
import com.example.jambubble_client.data.model.RemoveTrackRequest
import com.example.jambubble_client.data.model.ReorderPlaylistRequest
import com.example.jambubble_client.data.model.SpotifyTrackSearchResult
import com.example.jambubble_client.data.network.SignalRManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class MusicSessionViewModel : ViewModel(){
    private val signalRManager = SignalRManager(ApiConfig.BASE_URL)
    private val _sessionId = MutableStateFlow<String?>(null)
    val sessionId: StateFlow<String?> = _sessionId

    private val _guestUrl = MutableStateFlow<String?>(null)
    val guestUrl: StateFlow<String?> = _guestUrl

    private val _playlist = MutableStateFlow<List<PlaylistItem>>(emptyList())
    val playlist: StateFlow<List<PlaylistItem>> = _playlist

    private val _searchResults = MutableStateFlow<List<SpotifyTrackSearchResult>>(emptyList())
    val searchResults: StateFlow<List<SpotifyTrackSearchResult>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isSessionActive = MutableStateFlow(false)
    val isSessionActive: StateFlow<Boolean> = _isSessionActive

    private val hostUserId = "HOST_USER_ID"

    init{
        viewModelScope.launch{
            signalRManager.playlist.collect { playlist ->
                _playlist.value = playlist
            }
        }

        viewModelScope.launch {
            signalRManager.sessionClosed.collect { closed ->
                if (closed) {
                    _isSessionActive.value = false
                    _errorMessage.value = "セッションが終了しました"
                }
            }
        }
    }

    fun connectToServer(){
        viewModelScope.launch{
            try{
                signalRManager.connect()
            }catch(e:Exception){
                _errorMessage.value = "サーバーへの接続に失敗しました: ${e.message}"
                Log.e("ViewModel", "接続エラー", e)
            }
        }
    }

    fun createSession(deviceId:String){
        viewModelScope.launch{
            _isLoading.value = true
            _errorMessage.value = null

            try{
                val request = CreateSessionRequest(deviceId)
                val response = signalRManager.createSession(request)

                if(response != null){
                    _sessionId.value = response.sessionId
                    _guestUrl.value = response.guestUrl
                    _isSessionActive.value = true
                    Log.d("ViewModel", "セッション作成: ${response.sessionId}")
                }else {
                    _errorMessage.value = "セッションの作成に失敗しました"
                    Log.e("ViewModel", "セッション作成エラー")
                }
            }catch (e:Exception){
                _errorMessage.value = "エラー: ${e.message}"
                Log.e("ViewModel", "セッション作成エラー", e)
            }finally {
                _isLoading.value = false
            }
        }
    }


    //サーチトラック関数はRepositoryのを使用

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
                    requestedByUserId = hostUserId
                )

                val success = signalRManager.addTrack(request)
                if (!success) {
                    _errorMessage.value = "曲の追加に失敗しました"
                }
            } catch (e: Exception) {
                _errorMessage.value = "エラー: ${e.message}"
                Log.e("ViewModel", "Add track error", e)
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
                    requestedByUserId = hostUserId
                )

                val success = signalRManager.removeTrack(request)
                if (!success) {
                    _errorMessage.value = "曲の削除に失敗しました"
                }
            } catch (e: Exception) {
                _errorMessage.value = "エラー: ${e.message}"
                Log.e("ViewModel", "Remove track error", e)
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
                if (!success) {
                    _errorMessage.value = "並び替えに失敗しました"
                }
            } catch (e: Exception) {
                _errorMessage.value = "エラー: ${e.message}"
                Log.e("ViewModel", "Reorder error", e)
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
                }
            } catch (e: Exception) {
                _errorMessage.value = "セッションの終了に失敗しました: ${e.message}"
                Log.e("ViewModel", "Close session error", e)
            }
        }
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        signalRManager.disconnect()
    }

}


















