package com.example.jambubble_client.ui.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.R


@Composable
fun HelpScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 🔹 背景レイヤー（blur + 黒透過）
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x65000000))
                .blur(15.dp)
        )

        // 🔹 メニュー本体レイヤー（blurの影響を受けない）
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
        ) {

            // × ボタン
            IconButton(
                onClick = { navController.navigate("app/main") },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp)
                    .size(28.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.x),
                    contentDescription = "閉じる",
                    tint = Color.White
                )
            }


            Spacer(modifier = Modifier.size(20.dp))

            DialogMenuItem("アプリについて") {
                navController.navigate("app/user/profile")

            }

            DialogMenuItem("お問合せ") {
                navController.navigate("app/user/setting")

            }

            DialogMenuItem("規約") {
                navController.navigate("app/user/help")

            }
        }
    }
}

