package com.example.jambubble_client.ui.screens.users

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.buttons.ReturnButton

@Composable
fun SettingScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp) // 任意
    ) {
        ReturnButton(label = "Setting", onClick = {navController.navigate("app/main")})

        Spacer(modifier = Modifier.height(30.dp))
        Text("ナイトモード:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(30.dp))
        Text("位置情報:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(30.dp))
        Text("認証情報:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(30.dp))
        Text("フレンド:", style = MaterialTheme.typography.titleMedium)
    }
}
