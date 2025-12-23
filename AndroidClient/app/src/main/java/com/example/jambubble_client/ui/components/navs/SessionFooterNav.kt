package com.example.jambubble_client.ui.components.navs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.screens.features.ScreenState

@Composable
fun SessionFooterNav(onChangeState:(ScreenState) -> Unit){
    Box(        modifier = Modifier
        .fillMaxSize().padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(modifier = Modifier
            .width(216.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.Black)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier.size(60.dp)
                    .clickable{onChangeState(ScreenState.LINK)},
                contentAlignment = Alignment.Center
                ){
                Image(
                    painter = painterResource(id = R.drawable.qr_code),
                    contentDescription = "link",
                    modifier = Modifier.size(24.dp)
                )
            }
            Box(
                modifier = Modifier.size(60.dp)
                    .clickable{onChangeState(ScreenState.SEARCH)},
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.git_pull_request_create_arrow),
                    contentDescription = "link",
                    modifier = Modifier.size(24.dp)
                )
            }
            Box(
                    modifier = Modifier.size(60.dp)
                        .clickable{onChangeState(ScreenState.PLAYLIST)},
                contentAlignment = Alignment.Center
            ){
            Image(
                painter = painterResource(id = R.drawable.list_minus),
                contentDescription = "link",
                modifier = Modifier.size(24.dp)
            )
        }

        }
    }
}
