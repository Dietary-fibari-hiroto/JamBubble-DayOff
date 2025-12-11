package com.example.jambubble_client.ui.screens.musics

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

@Composable
fun FornowScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.offn),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.8f)
                .blur(15.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ReturnButton(onClick = {}, label = "")

            Spacer(Modifier.height(50.dp))

            TimeBar()

            AlbumSection()

            OperationSection()

            MessageSection()
        }
    }
}

@Composable
fun TimeBar() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 20.dp)
    ) {

        // White horizontal line
        Box(
            Modifier
                .width(300.dp)
                .height(2.dp)
                .background(Color.White)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0:02", color = Color.White, fontSize = 12.sp)
            Text("-0:30", color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun AlbumSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.offn),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
        )
    }
}


@Composable
fun OperationSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "熱りが冷めやらぬうちに",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                "goethe",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }

        Icon(
            painter = painterResource(R.drawable.heart),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp)
                .size(28.dp)
        )
    }
}
@Composable
fun MessageSection() {
    Box(
        modifier = Modifier
            .width(340.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x88000000))
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.offn),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Shared by", fontSize = 12.sp, color = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("ゆずき", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "恋の記憶を映画のように静かに映し出す、甘くて切ないチルソウル曲。\n\n" +
                        "柔らかなギターとジャジーなビートが心地よく、夜のひとときや物思いにふける瞬間にぴったりの“映像的”なサウンドが特徴だよ。",
                fontSize = 12.sp,
                color = Color.White
            )
        }
    }
}
