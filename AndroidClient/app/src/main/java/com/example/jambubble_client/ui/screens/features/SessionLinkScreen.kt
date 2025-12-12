package com.example.jambubble_client.ui.screens.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.elements.Tag
import com.example.jambubble_client.ui.styles.ColorAppleMusic

@Composable
fun SessionLinkScreen(
    navController: NavController
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.photo1), // images/photos/photo1.jpg
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.8f),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.4f)
                )
                .blur(15.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "リンク/QRコード",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------------------
            // URLバー
            // -------------------------------------
            Row(
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp)
                    .border(1.dp, Color(0xFF555555), RoundedCornerShape(10.dp))
                    .background(Color.Black),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "dawn-waiting.com/jumbubble/session/...",
                    color = Color(0xFFAAAAAA),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 12.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // -------------------------------------
            // QRコード画像
            // -------------------------------------
            Image(
                painter = painterResource(id = R.drawable.testqr),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // -------------------------------------
            // データ表示エリア
            // -------------------------------------
            Column(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                InfoRow("パスワード", "・・・・")
                InfoRow("タイトル:", "夜景ドライブ", big = true)
                InfoRow("シーン:", "夜景ドライブ")

                // タグ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("タグ:", color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Tag("tet")
                    Spacer(modifier = Modifier.width(8.dp))
                    Tag("test")
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Submit(
                backgroundColor = ColorAppleMusic,
                label = "セッションを終了する",
                onClick={}
            )
        }

        IconButton(
            onClick = {navController.navigate("function/session/member")},
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.TopStart)
        ) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.contact),
                contentDescription = null
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, big: Boolean = false) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = if (big) 20.sp else 14.sp,
            fontWeight = if (big) FontWeight.Bold else FontWeight.Normal
        )
    }
}
