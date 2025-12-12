package com.example.jambubble_client.ui.screens.sessions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.UserIcon
import com.example.jambubble_client.ui.components.pannels.SessionDeck
import com.example.jambubble_client.ui.styles.ColorPrimary
import com.example.jambubble_client.ui.styles.ColorSecondary

@Composable
fun SessionScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // --- Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Session",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            UserIcon(navController = navController)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- Menu Section ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Create Session Button
            Button(
                onClick = {navController.navigate("app/session/create")},
                modifier = Modifier
                    .size(width = 330.dp, height = 200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                ColorSecondary, // var(--color-secondary-bg) 想定
                                Color.Black
                            ),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .padding(end = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.shell_big),
                            contentDescription = null,
                            modifier = Modifier.matchParentSize()
                        )

                        Text(
                            text = "+",
                            color = Color(0xFF333333), // var(--color-deep-gray)
                            fontSize = 42.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset((-4).dp, (-4).dp)
                        )
                    }

                    Text(
                        text = "新しいSessionを\n開始する",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Search Session Button
            Button(
                onClick =  {},
                modifier = Modifier
                    .size(width = 330.dp, height = 100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                ColorPrimary, // var(--color-primary) を仮定
                                Color.Black
                            )
                        )
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.radio),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = "Sessionをさがす",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // --- SessionDecks ---
        SessionDeck(title = "フレンドのセッション")
        SessionDeck(title = "人気の公開セッション")
    }
}
