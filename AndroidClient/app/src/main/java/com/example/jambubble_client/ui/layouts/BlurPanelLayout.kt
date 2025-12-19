package com.example.jambubble_client.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BlurPanelLayout(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    // 画面全体を覆う
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)) // #00000065 に近い
            .blur(15.dp) // backdrop-filter: blur(15px)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() }
    ) {
        // 子要素 (ChildContent)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)   // CSS の padding-top:100px
                .clickable(enabled = false) {} // 背景クリックを透過させない
        ) {
            content()
        }
    }
}
