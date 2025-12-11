package com.example.jambubble_client.ui.screens.sessions

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
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
import com.example.jambubble_client.ui.components.elements.Tag
import com.example.jambubble_client.ui.styles.ColorSecondary

@Composable
fun SessionDetailScreen(
    navController: NavController
) {

    Box(modifier = Modifier.fillMaxSize()) {

        // ---- 背景画像（ブラー前） ----
        Image(
            painter = painterResource(id = R.drawable.photo1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.8f),
            contentScale = ContentScale.Crop
        )

        // ---- ブラー付き前面コンテナ ----
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .blur(15.dp)
        ) {}

        // ---- メインコンテンツ ----
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Cancel ボタン（左上）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            ) {
                ReturnButton(
                    label = "戻る",
                    onClick = { navController.navigate("app/session/search") }
                )
            }

            // ---- アルバム・タイトル ----
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.photo1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "秋の夜に合いそうな曲集めてます",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---- データ一覧 ----
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // シーン
                DataRow(label = "シーン:", value = "お酒飲みながら")

                // タグ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Text("タグ:", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(10.dp))
                    Tag(text="test")
                    Spacer(modifier = Modifier.width(10.dp))
                    Tag(text = "tes3")
                }

                // プロバイダ（1）
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Text("プロバイダ:", fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.width(10.dp))
                    Image(
                        painter = painterResource(R.drawable.applemusic_icon),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // プロバイダ（2）
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Text("プロバイダ:", fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.width(10.dp))
                    Image(
                        painter = painterResource(R.drawable.offn),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("こうき", fontSize = 16.sp, color = Color.White)
                }

                // 説明文
                Text(
                    text = "お酒飲みながら聴けるようなゆっくりめの曲探してます。ChillとかLofiのおすすめたくさん入れてください～",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ---- 参加ボタン ----
            Submit(
                label = "参加",
                backgroundColor= ColorSecondary,
                onClick = { navController.navigate("app/session/search/detail") }
            )
        }
    }
}


@Composable
fun DataRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.width(10.dp))
        Text(value, fontSize = 16.sp, color = Color.White)
    }
}
