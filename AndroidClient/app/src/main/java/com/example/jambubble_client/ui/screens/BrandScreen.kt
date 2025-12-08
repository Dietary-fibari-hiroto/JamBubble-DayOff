package com.example.jambubble_client.ui.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.estimateAnimationDurationMillis
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import kotlinx.coroutines.delay

@Composable
fun BrandScreen(navController: NavController){
    var isHidden by remember { mutableStateOf(true) }
    //アニメーションでOpacity(=alpha)を変化させる
    val alpha by animateFloatAsState(
        targetValue = if(isHidden) 0f else 1f,
        animationSpec = tween(durationMillis=1000, easing = LinearOutSlowInEasing)
    )

    //初回compose時のアニメーションを記述
    LaunchedEffect(Unit){
        isHidden = false
        delay(3000)
        isHidden = true
        delay(1000)
        navController.navigate("entrance/loading")
    }

    Box(
        modifier = Modifier // "mofifier"から"modifier"に修正
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ){
        Image(
            // "painterResource"に修正し、正しいリソースIDを指定
            painter = painterResource(id = R.drawable.dayoffwhitelogo),
            contentDescription = null,
            modifier = Modifier
                .size(width=150.dp,height=117.dp)
                .alpha(alpha)
        )
    }
}

