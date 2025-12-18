package com.example.jambubble_client.ui.components.navs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R

@Composable
fun FloatingFooterNav(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        // Glass Background
        Box(
            modifier = Modifier
                .width(360.dp)
                .height(80.dp)
        ) {

            // ガラス背景（ぼかし）
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.3f))
                    .blur(18.dp)
            )

            // アイコン行
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .padding(bottom = 6.dp)
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                FooterItem(
                    icon = R.drawable.house,
                    label = "ホーム",
                    onClick = { navController.navigate("app/main") }
                )

                FooterItem(
                    icon = R.drawable.list_minus,
                    label = "プレイリスト",
                    onClick = { navController.navigate("app/playlist") }
                )

                FooterItem(
                    icon = R.drawable.search,
                    label = "検索",
                    onClick = { navController.navigate("app/search") }
                )

                FooterItem(
                    icon = R.drawable.users,
                    label = "フレンド",
                    onClick = { navController.navigate("app/friend") }
                )

                FooterItem(
                    icon = R.drawable.shell,
                    label = "セッション",
                    onClick = { navController.navigate("app/session") }
                )
            }
        }
    }
}

@Composable
fun FooterItem(
    icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(65.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
