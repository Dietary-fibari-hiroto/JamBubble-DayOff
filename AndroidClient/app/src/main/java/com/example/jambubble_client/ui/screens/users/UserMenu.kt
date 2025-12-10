package com.example.jambubble_client.ui.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.buttons.ReturnButton


@Composable
fun UserMenu(
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
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ReturnButton(
                label = "Help",
                onClick = { navController.navigate("app/main") }
            )


            Spacer(modifier = Modifier.size(20.dp))

            DialogMenuItem("プロフィールを見る") {
                navController.navigate("app/user/profile")

            }

            DialogMenuItem("設定") {
                navController.navigate("app/user/setting")

            }

            DialogMenuItem("ヘルプ") {
                navController.navigate("app/user/help")

            }
        }
    }
}


@Composable
fun DialogMenuItem(
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clickable { onClick() }
            .padding(0.dp,10.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 5.dp)
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

