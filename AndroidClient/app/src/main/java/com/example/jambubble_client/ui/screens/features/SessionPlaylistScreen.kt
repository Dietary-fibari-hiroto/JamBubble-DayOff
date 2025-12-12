package com.example.jambubble_client.ui.screens.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R

@Composable
fun SessionPlaylistScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MusicListLabel(title = "blanket", artist = "Frad & Hyne")
        MusicListLabel(title = "blanket", artist = "Frad & Hyne")
        MusicListLabel(title = "blanket", artist = "Frad & Hyne", isPlay = true)
        MusicListLabel(title = "blanket", artist = "Frad & Hyne")
        MusicListLabel(title = "blanket", artist = "Frad & Hyne")
        MusicListLabel(title = "blanket", artist = "Frad & Hyne")
        MusicListLabel(title = "blanket", artist = "Frad & Hyne")
        MusicListLabel(title = "blanket", artist = "Frad & Hyne")
    }
}

@Composable
fun MusicListLabel(
    title: String,
    artist: String,
    isPlay: Boolean = false,
    albumPainter: Painter = painterResource(id = R.drawable.movie), // 任意
    playIcon: Painter = painterResource(id = R.drawable.shell),
    menuIcon: Painter = painterResource(id = R.drawable.menu),
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(if (isPlay) 1f else 0.8f)
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 20.dp,
                bottom = if (isPlay) 20.dp else 0.dp
            )
            .border(1.dp, Color.White, RectangleShape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // ===== Left: Album (only when isPlay) =====
        if (isPlay) {
            Image(
                painter = albumPainter,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // ===== Center Text =====
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 10.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = artist,
                color = Color.LightGray,
                fontSize = 12.sp
            )
        }

        // ===== Right-side Icon =====
        Image(
            painter = if (isPlay) playIcon else menuIcon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}
