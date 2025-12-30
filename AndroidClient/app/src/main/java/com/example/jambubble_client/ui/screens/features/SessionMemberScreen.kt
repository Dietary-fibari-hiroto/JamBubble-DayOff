package com.example.jambubble_client.ui.screens.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.cards.MemberCard
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel

@Composable
fun SessionMemberScreen(navController: NavController,sessionViewModel: MusicSessionViewModel,onChangeState: (ScreenState) -> Unit) {
    val guests by sessionViewModel.guests.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReturnButton(label="", onClick = {onChangeState(ScreenState.LINK)})

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.width(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(guests) { guest ->
                MemberCard(guest)
            }
        }
    }
}

