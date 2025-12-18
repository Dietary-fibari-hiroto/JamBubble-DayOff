package com.example.jambubble_client


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyAuthRepository
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyRepository
import com.example.jambubble_client.spotifyremote.service.SpotifyAuthService
import com.example.jambubble_client.ui.App
import com.example.jambubble_client.ui.styles.AppTheme
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelModelFactory
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private lateinit var authManager: SpotifyAuthRepository
    companion object {
        private const val TAG = "MainActivity"
    }


    //画面遷移しても音楽の再生状態が失われないようにSpotifyのサービスをここで定義
    private val musicPannelViewModel: MusicPannelViewModel by viewModels{
        MusicPannelModelFactory(SpotifyRepository(application))
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyAuthService::class.java)

        authManager = SpotifyAuthRepository(this, api)

        handleDeepLink(intent)

        setContent{
            AppTheme {
                App(musicPannelViewModel,authManager)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val code = intent.data?.getQueryParameter("code") ?: return
        lifecycleScope.launch {
            authManager.handleAuthCode(code)
        }
    }


}