package com.example.jambubble_client.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import com.example.jambubble_client.R

@Composable
fun EntranceLayout(content : @Composable () -> Unit){
    Box(
modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.pexels_thevibrantmachine_3066867),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier= Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha=0.3f)
                        )
                    )
                )
        )

        //中身
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            content()
        }
    }

}