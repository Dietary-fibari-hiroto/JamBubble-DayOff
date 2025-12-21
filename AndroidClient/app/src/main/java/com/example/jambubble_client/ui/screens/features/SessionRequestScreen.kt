package com.example.jambubble_client.ui.screens.features

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.inputs.SearchBar
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel

@Composable
fun SessionRequestScreen(navController: NavController,sessionViewModel: MusicSessionViewModel) {

    var isLibrarySelected by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // ===== タイトル =====
        Text(
            text = "リクエスト",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(20.dp))

        // ===== スイッチボタンコンテナ =====
        Row(
            modifier = Modifier
                .width(220.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF444444)), // deep-gray想定
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 楽曲検索ボタン
            SwitchButton(
                text = "楽曲検索",
                isActive = !isLibrarySelected,
                onClick = { isLibrarySelected = false }
            )

            // マイライブラリボタン
            SwitchButton(
                text = "マイライブラリ",
                isActive = isLibrarySelected,
                onClick = { isLibrarySelected = true }
            )
        }

        Spacer(Modifier.height(20.dp))

        // ===== SearchBar（既に作成済み） =====
        SearchBar(
            placeholder = "検索"
        )
    }
}

@Composable
fun SwitchButton(
    text: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(110.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isActive) Color.White else Color.Transparent
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isActive) Color.Black else Color.White
        )
    }
}
