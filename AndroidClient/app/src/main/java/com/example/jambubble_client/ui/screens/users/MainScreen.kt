package com.example.jambubble_client.ui.screens.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.UserIcon
import com.example.jambubble_client.ui.components.cards.SongCard
import com.example.jambubble_client.ui.components.pannels.SessionDeck
import com.example.jambubble_client.ui.viewmodel.users.MainUiState
import com.example.jambubble_client.ui.viewmodel.users.MainViewModel
import com.example.jambubble_client.ui.viewmodel.users.MainViewModelFactory


@Composable
fun MainScreen(
    navController: NavController
) {

    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(LocalContext.current)
    )
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.loadDataLists()
    }


    Column {

        // ヘッダー
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.jumbubblelogo),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Text(
                "JamBubble",
                style = MaterialTheme.typography.titleLarge, fontWeight = MaterialTheme.typography.titleLarge.fontWeight
            )
            UserIcon(
                navController = navController
            )
        }

        Spacer(Modifier.height(20.dp))

        // セッションデッキ
        when(uiState){
            is MainUiState.Loading -> {
                CircularProgressIndicator()
            }
            is MainUiState.Success -> {
                val state = uiState as MainUiState.Success
                SessionDeck(
                    title = "人気の公開セッション",
                    sessions = state.favoriteSessionList
                )
                SessionDeck(
                    title = "フレンドのセッション",
                    sessions = state.friendSessionList
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Favorite Song",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.favoriteMusicList) { song ->
                            SongCard(song)
                        }
                    }
                }
            }
            is MainUiState.Error -> {
                val message = (uiState as MainUiState.Error).message
                println(message)
            }
        }




        // Favorite Song

    }
}
