package com.example.jambubble_client.ui.screens.friends

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.elements.Tag

@Composable
fun InviteScreen(
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f)
                .align(Alignment.Center)
                .background(Color.White.copy(alpha = 0.53f))
                .clip(RoundedCornerShape(10.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // CancelButton（仮）
            IconButton(onClick = {}, modifier = Modifier.align(Alignment.End)) {
                Icon(Icons.Default.Close, contentDescription = "Cancel")
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = "フレンドから招待が届きました。",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(10.dp))

            // Album Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.photo1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "秋の夜に合いそうな曲集めてます",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(20.dp))

            // Data Container
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("シーン:", fontSize = 12.sp)
                    Spacer(Modifier.width(10.dp))
                    Text("お酒飲みながら", fontSize = 14.sp)
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("タグ:", fontSize = 12.sp)
                    Spacer(Modifier.width(10.dp))
                    Tag(text = "test")
                    Spacer(Modifier.width(10.dp))
                    Tag(text = "test")

                }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("プロバイダ:", fontSize = 12.sp)
                        Spacer(Modifier.width(10.dp))
                        Image(
                            painter = painterResource(id = R.drawable.applemusic_icon),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("プロバイダ:", fontSize = 12.sp)
                        Spacer(Modifier.width(10.dp))
                        Image(
                            painter = painterResource(id = R.drawable.offn),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text("こうき", fontSize = 14.sp)
                    }

                    Text(
                        text = "お酒飲みながら聴けるようなゆっくりめの曲探してます。ChillとかLofiのおすすめたくさん入れてください～",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 10.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Buttons
                Button(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1DB954) // spotify-primary 相当
                    )
                ) {
                    Text("参加する")
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray // deep-gray 相当
                    )
                ) {
                    Text("キャンセル")
                }
            }
        }
    }
