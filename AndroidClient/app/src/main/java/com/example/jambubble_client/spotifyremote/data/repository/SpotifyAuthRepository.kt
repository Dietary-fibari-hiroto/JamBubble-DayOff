package com.example.jambubble_client.spotifyremote.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.jambubble_client.Config
import com.example.jambubble_client.spotifyremote.service.SpotifyAuthService
import com.example.jambubble_client.spotifyremote.service.TokenResponse
import com.example.jambubble_client.util.PkceUtil
import com.example.jambubble_client.util.SecureStorage

class SpotifyAuthRepository(
    private val context: Context,
    private val api: SpotifyAuthService
) {

    fun buildAuthUri(): Uri {
        val verifier = PkceUtil.generateCodeVerifier()
        SecureStorage.save(context, Config.KEY_CODE_VERIFIER, verifier)

        val challenge = PkceUtil.generateCodeChallenge(verifier)

        return Uri.parse(Config.SPOTIFY_AUTH_URL).buildUpon()
            .appendQueryParameter("client_id", Config.SPOTIFY_CLIENT_ID)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", Config.SPOTIFY_REDIRECT_URL)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("code_challenge", challenge)
            .appendQueryParameter("scope", "user-read-private user-read-email")
            .build()
    }

    suspend fun handleAuthCode(code: String) {
        val verifier = SecureStorage.load(context, Config.KEY_CODE_VERIFIER) ?: return

        val response = api.getToken(
            grantType = "authorization_code",
            code = code,
            redirectUri = Config.SPOTIFY_REDIRECT_URL,
            clientId = Config.SPOTIFY_CLIENT_ID,
            codeVerifier = verifier
        )

        saveTokens(response)
    }

    private fun saveTokens(response: TokenResponse) {
        val expiresAt = System.currentTimeMillis() + response.expiresIn * 1000

        SecureStorage.save(context, Config.SPOTIFY_ACCESS_TOKEN, response.accessToken)
        response.refreshToken?.let {
            SecureStorage.save(context, Config.SPOTIFY_REFRESH_TOKEN, it)
        }
        SecureStorage.save(context, Config.KEY_TOKEN_EXPIRY, expiresAt.toString())

        Log.d("トークン","最後まで行ったよ:${response.accessToken}")
    }

    fun logout() {
        SecureStorage.remove(context, Config.SPOTIFY_ACCESS_TOKEN)
        SecureStorage.remove(context, Config.SPOTIFY_REFRESH_TOKEN)
        SecureStorage.remove(context, Config.KEY_TOKEN_EXPIRY)
    }
}


class TokenRefresher(
    private val context: Context,
    private val api: SpotifyAuthService,
    private val authManager: SpotifyAuthRepository
) {

    suspend fun getValidAccessToken(): String? {
        val expiresAt = SecureStorage.load(context, Config.KEY_TOKEN_EXPIRY)?.toLongOrNull()
        val accessToken = SecureStorage.load(context, Config.SPOTIFY_ACCESS_TOKEN)

        if (accessToken != null && expiresAt != null && System.currentTimeMillis() < expiresAt) {
            return accessToken
        }

        val refreshToken = SecureStorage.load(context, Config.SPOTIFY_REFRESH_TOKEN)
            ?: run {
                authManager.logout()
                return null
            }

        return try {
            val response = api.refreshToken(
                grantType = "refresh_token",
                refreshToken = refreshToken,
                clientId = Config.SPOTIFY_CLIENT_ID
            )
            authManager.apply {
                SecureStorage.save(context, Config.SPOTIFY_ACCESS_TOKEN, response.accessToken)
                SecureStorage.save(
                    context,
                    Config.KEY_TOKEN_EXPIRY,
                    (System.currentTimeMillis() + response.expiresIn * 1000).toString()
                )
            }
            response.accessToken
        } catch (e: Exception) {
            authManager.logout()
            null
        }
    }
}
