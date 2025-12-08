package com.example.jambubble_client.ui.screens.entrance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.jambubble_client.R

@Composable
fun LoadingScreen (navController: NavController){

    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("entrance")
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.jumbubblelogo),
            contentDescription = null,
            modifier = Modifier.size(143.dp)
        )

        Spacer(modifier = Modifier.height(80.dp))

        Column (
            horizontalAlignment = Alignment.CenterHorizontally
            ){
            Text(
                text="JamBubble",
                fontSize = 32.sp,
                fontWeight= FontWeight.Bold,
                color= Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text="A shared playlist colored by everyone’s taste.",
                fontSize=14.sp,
                color=Color.White
            )
        }

        Spacer(modifier = Modifier.height(80.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier.size(width = 104.dp,height=39.dp)
                    .background(Color(0xFFCCCCCC))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Now loading...",
                fontSize = 14.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Version 1.0.0",
            fontSize = 14.sp,
            color = Color.White
        )

        }

    }
