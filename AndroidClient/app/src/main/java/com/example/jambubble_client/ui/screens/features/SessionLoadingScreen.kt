package com.example.jambubble_client.ui.screens.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SessionLoadingScreen(navController: NavController) {

    var isCompletedLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                isCompletedLoading = true
                scope.launch {
                    delay(2000)
                    navController.navigate("function/session/link")
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            if (!isCompletedLoading) {
                Image(
                    painter = painterResource(id = R.drawable.shell_big),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
                Text("サーバーにリクエストしています...")
            } else {
                Text("準備ができました！")
                Text("それではお楽しみください！")
            }
        }
    }
}
