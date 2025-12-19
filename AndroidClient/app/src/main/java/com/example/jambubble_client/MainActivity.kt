package com.example.jambubble_client


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jambubble_client.spotifyremote.data.remote.AuthInterceptor
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyAuthRepository
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyRepository
import com.example.jambubble_client.spotifyremote.data.repository.SpotifySearchRepository
import com.example.jambubble_client.spotifyremote.service.SpotifyApiService
import com.example.jambubble_client.ui.App
import com.example.jambubble_client.ui.styles.AppTheme
import com.example.jambubble_client.ui.viewmodel.auths.SpotifyAuthViewModel
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelModelFactory
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel
import com.example.jambubble_client.ui.viewmodel.searchs.SearchViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    //画面遷移しても音楽の再生状態が失われないようにSpotifyのサービスをここで定義
    private val musicPannelViewModel: MusicPannelViewModel by viewModels{
        MusicPannelModelFactory(SpotifyRepository(application))
    }
    // Repositories
    private lateinit var authRepository: SpotifyAuthRepository
    private lateinit var searchRepository: SpotifySearchRepository

    private var hasHandledIntent = false

    // ViewModels
    private val authViewModel: SpotifyAuthViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SpotifyAuthViewModel(authRepository) as T
            }
        }
    }

    private val searchViewModel: SearchViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SearchViewModel(searchRepository) as T
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Repositories初期化
        setupRepositories()

        // Deep Link処理
        handleIntent(intent)



        setContent{
            AppTheme {
                App(musicPannelViewModel,authViewModel,searchViewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent called")
        setIntent(intent)
        hasHandledIntent = false  // リセット
        handleIntent(intent)
    }
    /**
     * Intentを処理（Deep Link）
     */
    private fun handleIntent(intent: Intent?) {
        // ✅ 既に処理済みならスキップ
        if (hasHandledIntent) {
            Log.d(TAG, "Intent already handled")
            return
        }

        val action = intent?.action
        val data = intent?.data

        Log.d(TAG, "handleIntent - action: $action, data: $data")

        if (action == Intent.ACTION_VIEW && data != null) {
            Log.d(TAG, "Deep Link: $data")
            handleAuthCallback(data)
            hasHandledIntent = true  // 処理済みにマーク
        } else {
            Log.d(TAG, "Not a Deep Link intent")
        }
    }

    /**
     * 認証コールバック処理
     */
    private fun handleAuthCallback(uri: Uri) {
        val scheme = uri.scheme
        val host = uri.host

        Log.d(TAG, "handleAuthCallback - scheme: $scheme, host: $host")

        val code = uri.getQueryParameter("code")
        Log.d(TAG, "handleAuthCallback - code: ${code?.take(50)}...")

        if (scheme == "jambubble" && host == "callback") {
            if (code != null) {
                Log.d(TAG, "✅ 認証コールバックを処理します")
                authViewModel.handleAuthCallback(uri)
            } else {
                Log.e(TAG, "❌ codeパラメータがありません")
            }
        } else {
            Log.w(TAG, "❌ 不正なDeep Link: scheme=$scheme, host=$host")
        }
    }

    /**
     * Repositoriesをセットアップ
     */
    private fun setupRepositories() {
        // ✅ トークン取得用APIサービス（認証なし）
        val authApiService = createApiService(Config.SPOTIFY_ACCOUNTS_BASE_URL)

        // ✅ ユーザー情報取得用APIサービス（認証付き・後で初期化）
        val userApiService = createUserApiService()

        // AuthRepositoryを初期化
        authRepository = SpotifyAuthRepository(
            context = applicationContext,
            authApiService = authApiService,
            userApiService = userApiService
        )

        // 認証付きAPIサービス（検索用）
        val searchApiService = createAuthenticatedApiService()

        // SearchRepositoryを初期化
        searchRepository = SpotifySearchRepository(
            apiService = searchApiService
        )
    }

    /**
     * APIサービス作成（認証なし）
     */
    private fun createApiService(baseUrl: String): SpotifyApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(Config.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Config.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyApiService::class.java)
    }

    /**
     * ユーザーAPI用サービス（✅ api.spotify.comを使用）
     */
    private fun createUserApiService(): SpotifyApiService {
        val authInterceptor = AuthInterceptor {
            authRepository.getValidAccessToken()
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(Config.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Config.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Config.SPOTIFY_API_BASE_URL)  // ✅ https://api.spotify.com/
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyApiService::class.java)
    }

    /**
     * APIサービス作成（認証付き・検索用）
     */
    private fun createAuthenticatedApiService(): SpotifyApiService {
        val authInterceptor = AuthInterceptor {
            authRepository.getValidAccessToken()
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(Config.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Config.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Config.SPOTIFY_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyApiService::class.java)
    }
}