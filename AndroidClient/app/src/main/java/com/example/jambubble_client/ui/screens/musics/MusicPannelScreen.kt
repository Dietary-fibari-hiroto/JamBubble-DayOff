package com.example.jambubble_client.ui.screens.musics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.jambubble_client.R

@Composable
fun MusicPanelScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 背景画像
        Image(
            painter = painterResource(id = R.drawable.offn),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .alpha(0.8f),
            contentScale = ContentScale.Crop
        )

        // ぼかし
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(15.dp)
        )

        // コンテンツ
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
                .zIndex(5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ▼（下方向アイコン）
            Image(
                painter = painterResource(id = R.drawable.chevron_down),
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 50.dp)
                    .size(40.dp)
                    .graphicsLayer {
                        rotationZ = 180f
                    }
            )

            // 3点リーダー
            Row(
                modifier = Modifier
                    .width(300.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ellipsis),
                    contentDescription = "menu"
                )
            }

            Spacer(Modifier.height(20.dp))

            // アルバム画像
            Image(
                painter = painterResource(id = R.drawable.offn),
                contentDescription = "album",
                modifier = Modifier.size(200.dp)
            )

            Spacer(Modifier.height(20.dp))

            // 曲情報
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "熱りが冷めやらぬうちに",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        "goethe",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.heart),
                    contentDescription = "heart",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 30.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            // シャッフル & リピートボタン
            Row(
                modifier = Modifier
                    .width(300.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shuffle),
                    contentDescription = null
                )
                Spacer(Modifier.width(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.repeat),
                    contentDescription = null
                )
            }

            Spacer(Modifier.height(30.dp))

            // 時間バー
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(2.dp)
                        .background(Color.White)
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.width(300.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0:02", style = MaterialTheme.typography.bodySmall)
                    Text("-4:18", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(60.dp))

            // 再生コントロール
            Row(
                modifier = Modifier
                    .width(300.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.fast_forward),
                    contentDescription = null,
                    modifier = Modifier.graphicsLayer {
                        rotationZ = 180f
                    }
                )

                Image(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = null
                )

                Image(
                    painter = painterResource(id = R.drawable.fast_forward),
                    contentDescription = null
                )
            }

            Spacer(Modifier.height(60.dp))
        }
    }
}
