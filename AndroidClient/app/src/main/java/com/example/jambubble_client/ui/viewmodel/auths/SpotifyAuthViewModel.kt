package com.example.jambubble_client.ui.viewmodel.auths

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.spotifyremote.data.model.SpotifyUser
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyAuthRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 認証ViewModel
 */
class SpotifyAuthViewModel(
    private val authRepository: SpotifyAuthRepository
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn
    val currentUser: StateFlow<SpotifyUser?> = authRepository.currentUser

    /**
     * ログイン開始
     *
     * @param activity Activity context（ブラウザ起動に必要）
     */
    fun startLogin(activity: Activity) {
        authRepository.startLogin(activity)
    }

    fun handleAuthCallback(uri: Uri) {
        viewModelScope.launch {
            authRepository.handleAuthCallback(uri)
        }
    }

    fun logout() {
        authRepository.logout()
    }
}
