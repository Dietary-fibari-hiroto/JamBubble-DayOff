package com.example.jambubble_client.ui.components.pannels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AuthBgPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(0.9f)        // width: 90%
            .defaultMinSize(minHeight = (0.8f).let { 0.dp }) // min-height の代替
            .padding(vertical = 20.dp) // padding: 50px 0
            .clip(RoundedCornerShape(30.dp)) // border-radius: 30px
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x88000000), // #00000088
                        Color(0x88000000)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        content()
        Spacer(Modifier.height(20.dp))
    }
}
