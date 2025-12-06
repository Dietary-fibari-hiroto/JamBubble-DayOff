package com.example.jambubble_client.ui.screens.auths

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.pannels.AuthBgPanel

@Composable
fun EmailCompleteScreen(
    navController: NavController
) {

    AuthBgPanel{

        // container 相当
        Column(
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Image(
                painter = painterResource(id = R.drawable.jumbubblelogo),
                contentDescription = null,
                modifier = Modifier.size(143.dp)
            )

            Text(
                text = "メールアドレスを確認できました！",
                fontSize = 18.sp,
                color = Color.White
            )

            Submit(
                label = "次へ",
                onClick = {navController.navigate("auth/register/provider")}
            )
        }
    }
}
