package com.example.jambubble_client.ui.screens.auths

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.pannels.AuthBgPanel

@Composable
fun RegisterCompleteScreen(
    navController: NavController
) {
    AuthBgPanel {

        // ロゴ
        Image(
            painter = painterResource(id = R.drawable.jumbubblelogo),
            contentDescription = null,
            modifier = Modifier
                .size(143.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(25.dp))

            // タイトル
            Text(
                text = "アカウントを作成しました",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(25.dp))

            // 説明文
            Text(
                text = "ようこそJumBubbleへ\n" +
                        "ここから始まるのは、みんなの“好き”が集まった時間。\n" +
                        "曲を追加して、一緒にプレイリストを作っていきましょう。\n",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(35.dp))

            // ボタン
            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Submit(
                    label = "はじめる",
                    onClick = { navController.navigate("app/main") },
                )
            }
        }
    }
}
