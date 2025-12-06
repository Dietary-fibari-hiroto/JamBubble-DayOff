package com.example.jambubble_client.ui.components.elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jambubble_client.ui.styles.ColorPrimary

fun Modifier.topBubble(scope: BoxScope): Modifier =
    this
        .then(
            scope.run {
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-100).dp)
            }
        )

fun Modifier.bottomBubble(scope: BoxScope): Modifier =
    this.then(
        scope.run {
            Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (100).dp)
        }
    )
@Composable
fun Bubble(
    modifier: Modifier = Modifier,next: Boolean=false
    ){
    val scale by animateFloatAsState(
    targetValue = if (next) 1f else 0.7f,
    animationSpec = tween(600)
)

    Box(modifier = modifier.size(200.dp).border(1.dp,ColorPrimary,CircleShape))

}
