package com.example.jambubble_client.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.R

@Composable
fun MusicChip(
    title: String,
    artist: String,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = 10.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RectangleShape
            ).clickable{ navController.navigate("app/music/panel") },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Row {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = artist,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ellipsis),
            contentDescription = null
        )
    }
}
