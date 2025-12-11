package com.example.jambubble_client.ui.screens.sessions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.buttons.UserIcon
import com.example.jambubble_client.ui.components.cards.SessionCardBig

@Composable
fun SessionListScreen(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize()) {

        // --- 上部ボタン・アイコン ---
        Row(modifier = Modifier.fillMaxWidth()){
            ReturnButton(label = "人気のセッション", onClick = {navController.navigate("")})
            UserIcon(navController = navController)
        }


        // --- セッション一覧 ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SessionCardBig()
            SessionCardBig()
            SessionCardBig()
        }
    }
}
