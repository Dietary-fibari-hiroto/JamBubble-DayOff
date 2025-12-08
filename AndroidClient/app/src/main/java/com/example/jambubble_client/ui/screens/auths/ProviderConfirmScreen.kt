package com.example.jambubble_client.ui.screens.auths

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.pannels.AuthBgPanel
import com.example.jambubble_client.ui.styles.ColorAppleMusic
import com.example.jambubble_client.ui.styles.ColorDeepGray
import com.example.jambubble_client.ui.styles.ColorSpotifyPrimary

@Composable
fun ProviderConfirmScreen(
    navController: NavController
) {

    AuthBgPanel {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ロゴ
            Image(
                painter = painterResource(id = R.drawable.jumbubblelogo),
                contentDescription = null,
                modifier = Modifier
                    .size(143.dp)
                    .padding(top = 40.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            // タイトル
            Text(
                text = "プロバイダにログイン",
                fontSize = 22.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(15.dp))

            // 説明文
            Text(
                text = "アプリの主要な機能を使うにはプロバイダアカウントでログインしていただく必要があります。下記に当てはまるプロバイダへログインしてください。\n\nAppleMusic / Spotify",
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))

            // ボタン一覧
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Submit(
                    label = "Spotifyでログイン",
                    iconRes = R.drawable.spotify_icon,
                    backgroundColor = ColorSpotifyPrimary,
                    onClick = {navController.navigate("")}
                )

                Spacer(modifier = Modifier.height(10.dp))

                Submit(
                    label = "AppleMusicでログイン",
                    iconRes = R.drawable.applemusic_icon,
                    backgroundColor=ColorAppleMusic,
                    onClick = {navController.navigate("")}
                )

                Spacer(modifier = Modifier.height(10.dp))

                Submit(
                    label = "プロバイダなしで進む",
                    iconRes = null,
                    backgroundColor = ColorDeepGray,
                    onClick = {navController.navigate("auth/register/complete")}
                )
            }
        }
    }
}
