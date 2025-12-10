package com.example.jambubble_client.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.jambubble_client.ui.screens.musics.PlaylistDetailScreen

@Preview(
    showBackground = true,
    backgroundColor = 0xFFffffff,
    heightDp = 1200
)
@Composable
fun PreviewUserProfile() {
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        PlaylistDetailScreen(navController)
    }
}