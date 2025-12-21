package com.example.jambubble_client.ui.screens.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.styles.ColorSecondaryBg
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel

@Composable
fun SessionRequestBoardScreen(
    navController: NavController,
    sessionViewModel: MusicSessionViewModel,
    title: String = "ただ君に晴れ",
    artist: String = "ヨルシカ"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        // ===== ReturnButton（作成済み） =====
        ReturnButton(label = "", onClick = {})

        // ===== 画像 + タイトル =====
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.movie),
                contentDescription = null,
                modifier = Modifier
                    .size(225.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = artist,
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }

        // ===== Submit ボタン（作成済み） =====
        Submit(
            label = "リクエスト",
            backgroundColor = ColorSecondaryBg,
            iconRes =R.drawable.arrow_up_from_line, // アイコン
            onClick = {}
        )
    }
}
