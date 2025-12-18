package com.example.jambubble_client.ui.screens.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jambubble_client.R
import com.example.jambubble_client.data.UserLocalDataStore
import com.example.jambubble_client.data.api.ApiConfig
import com.example.jambubble_client.ui.components.buttons.EditButton
import com.example.jambubble_client.ui.components.buttons.MiniSubmit
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.styles.ColorDeepGray
import com.example.jambubble_client.ui.styles.ColorPrimary

@Composable
fun UserProfileScreen(navController: NavController) {
    val user by UserLocalDataStore.userFlow.collectAsState(initial = null)


    when(user){
        null->{

        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ReturnButton(
                    label = "Profile",
                    onClick = { navController.navigate("app/main") }
                )

                Spacer(Modifier.height(60.dp))

                // プロフィール画像
                Box(
                    modifier = Modifier
                        .size(200.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model= ApiConfig.BASE_URL+user?.imgUrl,
                            placeholder = painterResource(R.drawable.dawn_cat),
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    EditButton(
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                // 名前
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Text(user!!.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.width(8.dp))
                    EditButton()
                }

                // 自己紹介文
                Text(
                    text = user?.message.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )


                // Favorite Song
                SectionFrame(title = "Favorite song") {

                    Box(
                        modifier = Modifier
                            .size(150.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Image(
                            painter = painterResource(R.drawable.movie),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.Center)
                        )
                        EditButton(modifier = Modifier.padding(8.dp))
                    }

                    Text(
                        "Movie",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        "Tom Misch・Spotify",
                        style = MaterialTheme.typography.bodySmall
                    )
                }


                MiniSubmit(label = "保存", color = ColorPrimary)


                // 過去の履歴
                SectionFrame(title = "過去の履歴") {

                    Text("セッション数", style = MaterialTheme.typography.bodyMedium)
                    Row {
                        Text(
                            "12",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(" 回", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // プロバイダ
                SectionFrame(title = "プロバイダ") {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        Submit(backgroundColor= ColorDeepGray,label = "Premiumプラン", iconRes = R.drawable.spotify_icon, onClick = {navController.navigate("auth/spotify/login")})
                        Submit(backgroundColor= ColorDeepGray,label = "Apple Music でログイン", iconRes = R.drawable.applemusic_icon, onClick = {})
                    }
                }

                Spacer(Modifier.height(50.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    MiniSubmit(label = "ログアウト", color = Color.DarkGray)
                    MiniSubmit(label = "アカウント削除", color = Color(0xFFF4535C))
                }

            }
        }

    }
}


@Composable
fun SectionFrame(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        content()
    }
}
