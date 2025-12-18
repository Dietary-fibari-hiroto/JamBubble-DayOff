package com.example.jambubble_client.ui.screens.auths

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyAuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyLoginScreen(
    authManager: SpotifyAuthRepository,
    navController: NavController
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    authManager.buildAuthUri()
                )
                context.startActivity(intent)
            }
        ) {
            Text("Spotifyでログイン")
        }
    }
}
