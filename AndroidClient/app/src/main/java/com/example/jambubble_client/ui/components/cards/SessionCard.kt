package com.example.jambubble_client.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.jambubble_client.R
import com.example.jambubble_client.data.api.ApiConfig
import com.example.jambubble_client.data.dto.SessionListResponseDto

@Composable
fun SessionCard(
    session: SessionListResponseDto = SessionListResponseDto(
        id = 1,
        title = "title",
        imgUrl="https://dawn-waiting.com/static/media/IMG_7018.f9a222d51c71b3498418.jpg",
        userCount = 0
    )
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .width(180.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(width = 180.dp, height = 120.dp)
                .clip(RoundedCornerShape(10.dp)),
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ApiConfig.BASE_URL+session.imgUrl,
                    placeholder =painterResource(R.drawable.dawn_cat),
                    error = painterResource(R.drawable.dawn_cat)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Image(
                painter = painterResource(R.drawable.dawn_cat),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
            )
        }

        Text(
            text = session.title,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
