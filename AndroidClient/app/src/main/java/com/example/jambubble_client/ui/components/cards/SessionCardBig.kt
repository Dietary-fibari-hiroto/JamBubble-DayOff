package com.example.jambubble_client.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
fun SessionCardBig() {

    Column(
        modifier = Modifier
            .width(320.dp)
            .height(239.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // 画像枠
        Box(
            modifier = Modifier
                .width(320.dp)
                .height(213.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.offn),
                contentDescription = "thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Image(
                painter = painterResource(id = R.drawable.spotify_icon),
                contentDescription = "provider",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .clip(CircleShape)
            )
        }

        Text(
            "秋の夜に合いそうな曲集めてます",
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
        )
    }
}
