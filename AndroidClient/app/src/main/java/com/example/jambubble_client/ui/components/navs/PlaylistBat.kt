package com.example.jambubble_client.ui.components.navs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jambubble_client.R

@Composable
fun PlaylistBar(
    title: String,
    provider: String,
    userName: String,
    date: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(70.dp)
            .clickable { onClick() }
            .padding(bottom = 5.dp),
        verticalAlignment = Alignment.Bottom
    ) {

        // --- 左の画像 ---
        Image(
            painter = painterResource(id = R.drawable.offn),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(10.dp))

        // --- データ ---
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.White
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$userName・$provider",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = date,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
