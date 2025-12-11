package com.example.jambubble_client.ui.screens.musics

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.buttons.UserIcon
import com.example.jambubble_client.ui.components.navs.PlaylistBar

@Composable
fun PlaylistScreen(
    navController: NavController
) {
    var isViewMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingBar(
                isViewMode = isViewMode,
                onPlaylist = { isViewMode = false },
                onCanSave = { isViewMode = true }
            )

            UserIcon(navController = navController)
        }

        Spacer(Modifier.height(30.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            repeat(10) {
                PlaylistBar(
                    title = "淡路遠征!!!",
                    provider = "Spotify",
                    userName = "加藤 勇作",
                    date = "2023/09/29",
                    onClick = {
                        navController.navigate("app/playlist/detail")
                    }
                )
            }
        }
    }
}

@Composable
fun SettingBar(
    isViewMode: Boolean,
    onPlaylist: () -> Unit,
    onCanSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .width(280.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF202020)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(30.dp)
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (!isViewMode) Color.White else Color.Transparent)
                .clickable { onPlaylist() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "プレイリスト",
                color = if (!isViewMode) Color.Black else Color.White
            )
        }

        Box(
            modifier = Modifier
                .width(140.dp)
                .height(30.dp)
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isViewMode) Color.White else Color.Transparent)
                .clickable { onCanSave() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "保存できるセッション",
                color = if (isViewMode) Color.Black else Color.White
            )
        }
    }
}

