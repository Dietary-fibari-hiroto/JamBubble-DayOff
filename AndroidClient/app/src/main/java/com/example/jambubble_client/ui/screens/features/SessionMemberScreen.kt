package com.example.jambubble_client.ui.screens.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.cards.MemberCard
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel

@Composable
fun SessionMemberScreen(navController: NavController,sessionViewModel: MusicSessionViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReturnButton(label="", onClick = {})

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.width(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MemberCard()
            MemberCard()
            MemberCard()
        }
    }
}

