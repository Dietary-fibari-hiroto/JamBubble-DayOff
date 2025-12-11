package com.example.jambubble_client.ui.screens.musics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.UserIcon
import com.example.jambubble_client.ui.components.inputs.SearchBar

@Composable
fun SearchScreen(navController: NavController) {
    var isApple by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isApple)
                    Brush.verticalGradient(
                        listOf(Color.Black, Color(0xFF721420))
                    )
                else
                    Brush.verticalGradient(
                        listOf(Color.Black, Color(0xFF1DB954))
                    )
            )
    ) {
        // ===== 上部入力エリア =====
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchBar(placeholder = "友達を検索")
                UserIcon(navController = navController)
            }

            Spacer(Modifier.height(20.dp))

            ProviderSwitch(
                isApple = isApple,
                onSpotify = { isApple = false },
                onAppleMusic = { isApple = true }
            )
        }

        // ===== 下部スクロールエリア =====
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)   // ← これが重要！
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp)
        ) {
            // 検索結果をここに書く
        }
    }
}


@Composable
fun ProviderButton(
    imageRes: Int,
    active: Boolean,
    onClick: () -> Unit
) {
    // 非アクティブ → グレースケール
    val colorMatrix = if (active) null else ColorMatrix().apply {
        setToSaturation(0f)
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(
                if (active) Color.White.copy(alpha = 0.1f)
                else Color.Transparent
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            colorFilter = colorMatrix?.let { ColorFilter.colorMatrix(it) }
        )
    }
}
@Composable
fun ProviderSwitch(
    isApple: Boolean,
    onSpotify: () -> Unit,
    onAppleMusic: () -> Unit
) {

    val bgColor = if (isApple) Color(0xFF721420) else Color(0xFF1DB954)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .size(width = 150.dp, height = 50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(bgColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            ProviderButton(
                imageRes = R.drawable.spotify_icon,
                active = !isApple,
                onClick = onSpotify
            )

            ProviderButton(
                imageRes = R.drawable.applemusic_icon,
                active = isApple,
                onClick = onAppleMusic
            )
        }
    }
}
