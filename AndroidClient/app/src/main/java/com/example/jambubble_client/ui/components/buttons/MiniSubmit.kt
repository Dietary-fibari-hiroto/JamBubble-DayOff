package com.example.jambubble_client.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MiniSubmit(
    label: String,
    color: Color,
    textColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(110.dp)
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
