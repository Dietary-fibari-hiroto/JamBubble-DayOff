package com.example.jambubble_client.ui.components.pannels

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.jambubble_client.R
import com.example.jambubble_client.data.dto.SessionListResponseDto
import com.example.jambubble_client.ui.components.cards.SessionCard


@Composable
fun SessionDeck(
    title: String,
    sessions: List<SessionListResponseDto> = emptyList(),
    thumbnail: Int = R.drawable.offn,
    provider: Int = R.drawable.spotify_icon,
    onMore: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {

        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            TextButton(onClick = onMore) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("もっとみる", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.width(4.dp))
                    ArrowRight()
                }
            }
        }

        // Cards Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sessions) { session ->
                SessionCard(
                    session = session
                )
            }
        }
    }
}

@Composable
fun ArrowRight() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .width(40.dp)
                .background(Color.White)
        )
        Canvas(modifier = Modifier.size(10.dp)) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, size.height / 2)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path, Color.White)
        }
    }
}
