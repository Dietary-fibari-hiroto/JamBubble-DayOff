package com.example.jambubble_client.spotifyremote.data.repository

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import com.example.jambubble_client.Config
import com.example.jambubble_client.spotifyremote.data.model.SpotifyTokens
import com.example.jambubble_client.spotifyremote.data.model.SpotifyUser
import com.example.jambubble_client.spotifyremote.service.SpotifyApiService
import com.example.jambubble_client.util.PKCEHelper
import com.example.jambubble_client.util.SecureStorage
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Spotify認証Repository
 *
 * SecureStorageでトークンを暗号化保存
 */
class SpotifyAuthRepository(
    private val context: Context,
    private val authApiService: SpotifyApiService,  // トークン取得用
    private val userApiService: SpotifyApiService   // ユーザー情報取得用（認証付き）
) {

    companion object {
        private const val TAG = "SpotifyAuthRepository"
        private const val KEY_TOKENS = Config.SPOTIFY_ACCESS_TOKEN
        private const val KEY_CODE_VERIFIER = Config.KEY_CODE_VERIFIER
        private const val KEY_USER = "spotify_user"
        private const val KEY_PROCESSING_CODE = "processing_code"  // 重複処理防止
    }

    private val mutex = Mutex()
    private val gson = Gson()

    // 状態
    private val _tokens = MutableStateFlow<SpotifyTokens?>(null)
    val tokens: StateFlow<SpotifyTokens?> = _tokens.asStateFlow()

    private val _currentUser = MutableStateFlow<SpotifyUser?>(null)
    val currentUser: StateFlow<SpotifyUser?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        loadSavedTokens()
    }

    /**
     * ログイン開始
     *
     * @param activity Activity context（ブラウザ起動に必要）
     */
    fun startLogin(activity: Activity) {
        try {
            // PKCE生成
            val codeVerifier = PKCEHelper.generateCodeVerifier()
            val codeChallenge = PKCEHelper.generateCodeChallenge(codeVerifier)

            // Code Verifier保存
            SecureStorage.save(context, KEY_CODE_VERIFIER, codeVerifier)
            Log.d(TAG, "Code Verifier保存: ${codeVerifier.take(20)}...")

            // 認証URL構築
            val authUrl = buildAuthUrl(codeChallenge)

            Log.d(TAG, "認証開始: $authUrl")

            // ✅ Activityコンテキストでブラウザを開く
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.launchUrl(activity, Uri.parse(authUrl))

        } catch (e: Exception) {
            Log.e(TAG, "ログイン開始エラー", e)
        }
    }

    /**
     * 認証URL構築
     */
    private fun buildAuthUrl(codeChallenge: String): String {
        return Uri.parse(Config.SPOTIFY_AUTH_URL).buildUpon()
            .appendQueryParameter("client_id", Config.SPOTIFY_CLIENT_ID)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", Config.SPOTIFY_REDIRECT_URL)
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("code_challenge", codeChallenge)
            .appendQueryParameter("scope", Config.SPOTIFY_SCOPES)
            .build()
            .toString()
    }

    /**
     * 認証コールバック処理
     */
    suspend fun handleAuthCallback(uri: Uri): Boolean {
        return mutex.withLock {
            try {
                val code = uri.getQueryParameter("code")
                if (code == null) {
                    Log.e(TAG, "Authorization codeがありません")
                    return@withLock false
                }

                // ✅ 重複処理チェック
                val lastProcessedCode = SecureStorage.load(context, KEY_PROCESSING_CODE)
                if (code == lastProcessedCode) {
                    Log.d(TAG, "このcodeは既に処理済みです")
                    return@withLock false
                }

                // 処理中のcodeを保存
                SecureStorage.save(context, KEY_PROCESSING_CODE, code)

                val codeVerifier = SecureStorage.load(context, KEY_CODE_VERIFIER)
                if (codeVerifier == null) {
                    Log.e(TAG, "Code Verifierがありません")
                    return@withLock false
                }

                Log.d(TAG, "Code Verifier取得: ${codeVerifier.take(20)}...")

                // トークン交換
                val success = exchangeCodeForToken(code, codeVerifier)

                // Code Verifier削除
                SecureStorage.remove(context, KEY_CODE_VERIFIER)

                if (success) {
                    fetchUserInfo()
                    // 処理完了後、処理済みcodeも削除
                    SecureStorage.remove(context, KEY_PROCESSING_CODE)
                }

                return@withLock success

            } catch (e: Exception) {
                Log.e(TAG, "コールバック処理エラー", e)
                return@withLock false
            }
        }
    }

    /**
     * Code → Token交換
     */
    private suspend fun exchangeCodeForToken(code: String, codeVerifier: String): Boolean {
        try {
            Log.d(TAG, "トークン交換開始")

            val response = authApiService.getAccessToken(
                grantType = "authorization_code",
                code = code,
                redirectUri = Config.SPOTIFY_REDIRECT_URL,
                clientId = Config.SPOTIFY_CLIENT_ID,
                codeVerifier = codeVerifier
            )

            if (response.isSuccessful && response.body() != null) {
                val tokenResponse = response.body()!!
                val tokens = SpotifyTokens(
                    accessToken = tokenResponse.accessToken,
                    tokenType = tokenResponse.tokenType,
                    expiresIn = tokenResponse.expiresIn,
                    refreshToken = tokenResponse.refreshToken,
                    scope = tokenResponse.scope
                )

                saveTokens(tokens)
                Log.d(TAG, "トークン取得成功:${tokens}")
                return true
            } else {
                Log.e(TAG, "トークン取得失敗: ${response.code()}")
                Log.e(TAG, "エラー詳細: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "トークン取得エラー", e)
        }

        return false
    }

    /**
     * トークン保存（SecureStorage使用）
     */
    private fun saveTokens(tokens: SpotifyTokens) {
        val json = gson.toJson(tokens)
        SecureStorage.save(context, KEY_TOKENS, json)
        _tokens.value = tokens
        _isLoggedIn.value = true
    }

    /**
     * 保存されたトークン読み込み
     */
    private fun loadSavedTokens() {
        try {
            val json = SecureStorage.load(context, KEY_TOKENS)
            if (json != null) {
                val tokens = gson.fromJson(json, SpotifyTokens::class.java)
                if (tokens.isValid()) {
                    _tokens.value = tokens
                    _isLoggedIn.value = true
                    loadSavedUser()
                    Log.d(TAG, "保存されたトークンを読み込みました")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "トークン読み込みエラー", e)
        }
    }

    /**
     * 保存されたユーザー情報読み込み
     */
    private fun loadSavedUser() {
        try {
            val json = SecureStorage.load(context, KEY_USER)
            if (json != null) {
                val user = gson.fromJson(json, SpotifyUser::class.java)
                _currentUser.value = user
            }
        } catch (e: Exception) {
            Log.e(TAG, "ユーザー情報読み込みエラー", e)
        }
    }

    /**
     * ユーザー情報取得
     * ✅ 認証付きAPIサービスを使用
     */
    private suspend fun fetchUserInfo() {
        try {
            Log.d(TAG, "ユーザー情報取得開始")

            val response = userApiService.getCurrentUser()

            if (response.isSuccessful && response.body() != null) {
                val userResponse = response.body()!!
                val user = SpotifyUser(
                    id = userResponse.id,
                    displayName = userResponse.displayName,
                    email = userResponse.email,
                    imageUrl = userResponse.images?.firstOrNull()?.url,
                    product = userResponse.product
                )

                _currentUser.value = user

                // 保存
                val json = gson.toJson(user)
                SecureStorage.save(context, KEY_USER, json)

                Log.d(TAG, "ユーザー情報取得成功: ${user.displayName}")
            } else {
                Log.e(TAG, "ユーザー情報取得失敗: ${response.code()}")
                Log.e(TAG, "エラー詳細: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "ユーザー情報取得エラー", e)
        }
    }

    /**
     * 有効なアクセストークン取得（自動リフレッシュ）
     */
    suspend fun getValidAccessToken(): String? {
        return mutex.withLock {
            val currentTokens = _tokens.value ?: return@withLock null

            if (currentTokens.isValid()) {
                return@withLock currentTokens.accessToken
            }

            // リフレッシュ
            if (refreshTokens()) {
                return@withLock _tokens.value?.accessToken
            }

            return@withLock null
        }
    }

    /**
     * トークンリフレッシュ
     */
    private suspend fun refreshTokens(): Boolean {
        try {
            val refreshToken = _tokens.value?.refreshToken
            if (refreshToken == null) {
                Log.e(TAG, "リフレッシュトークンがありません")
                return false
            }

            Log.d(TAG, "トークンリフレッシュ開始")

            val response = authApiService.refreshToken(
                grantType = "refresh_token",
                refreshToken = refreshToken,
                clientId = Config.SPOTIFY_CLIENT_ID
            )

            if (response.isSuccessful && response.body() != null) {
                val tokenResponse = response.body()!!
                val newTokens = SpotifyTokens(
                    accessToken = tokenResponse.accessToken,
                    tokenType = tokenResponse.tokenType,
                    expiresIn = tokenResponse.expiresIn,
                    refreshToken = refreshToken,  // 既存のを維持
                    scope = tokenResponse.scope
                )

                saveTokens(newTokens)
                Log.d(TAG, "トークンリフレッシュ成功")
                return true
            }
        } catch (e: Exception) {
            Log.e(TAG, "トークンリフレッシュエラー", e)
        }

        return false
    }

    /**
     * ログアウト
     */
    fun logout() {
        _tokens.value = null
        _currentUser.value = null
        _isLoggedIn.value = false

        SecureStorage.remove(context, KEY_TOKENS)
        SecureStorage.remove(context, KEY_USER)
        SecureStorage.remove(context, KEY_CODE_VERIFIER)
        SecureStorage.remove(context, KEY_PROCESSING_CODE)

        Log.d(TAG, "ログアウトしました")
    }
}