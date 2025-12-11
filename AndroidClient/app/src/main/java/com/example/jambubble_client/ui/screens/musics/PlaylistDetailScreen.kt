package com.example.jambubble_client.ui.screens.musics

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.MiniSubmit
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.cards.MusicChip
import com.example.jambubble_client.ui.styles.ColorPrimary
import com.example.jambubble_client.ui.styles.ColorSecondary

@Composable
fun PlaylistDetailScreen(
    navController: NavController
) {

    val isCanSave = remember { mutableStateOf(true) }

    //背景画像
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.offn),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .alpha(1f)
        )

        //ぼかし
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(15.dp)
        )

        //メインコンテンツ
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(5f).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //上部
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReturnButton(
                    label = "Back",
                    onClick = { navController.navigate("app/playlist") })
                Image(
                    painter = painterResource(id = R.drawable.ellipsis),
                    contentDescription = "menu"
                )
            }

            Spacer(Modifier.height(20.dp))

            //アルバム情報
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.offn),
                    contentDescription = "album",
                    modifier = Modifier
                        .size(200.dp)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "淡路遠征!!!",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Spacer(Modifier.height(10.dp))

                Text("加藤 勇作・Spotify", style = MaterialTheme.typography.bodyMedium)
                Text("2023/9/29", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(20.dp))

            // 再生・シャッフル・共有アイコン
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiniSubmit(label = "再生", color = colorResource(android.R.color.black))
                    Spacer(Modifier.width(10.dp))
                    MiniSubmit(label = "シャッフル再生", color = ColorSecondary)
                }

                Image(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = "share",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                )
            }

            Spacer(Modifier.height(20.dp))

            // セッション保存ボタン
            if (isCanSave.value) {
                MiniSubmit(
                    label = "セッションを保存",
                    color = ColorPrimary,
                    textColor = Color.Black
                )
            }

            Spacer(Modifier.height(20.dp))

            // 曲リスト
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                repeat(9) {
                    MusicChip(
                        title = "blanket",
                        artist = "Frad & Hyne",
                        navController = navController
                    )
                }
            }
        }
    }
}
