package com.example.jambubble_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyRepository
import com.example.jambubble_client.ui.App
import com.example.jambubble_client.ui.styles.AppTheme
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelModelFactory
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel

class MainActivity : ComponentActivity() {

    //画面遷移しても音楽の再生状態が失われないようにSpotifyのサービスをここで定義
    private val musicPannelViewModel: MusicPannelViewModel by viewModels{
        MusicPannelModelFactory(SpotifyRepository(application))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            AppTheme {
                App(musicPannelViewModel)
            }
        }

    }
}