package com.example.jambubble_client.ui.screens.features

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.navs.SessionFooterNav
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel

//スクリーンの切替ENUM
enum class ScreenState {
    LINK,
    SEARCH,
    PLAYLIST
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionFunctionScreen(viewModel: MusicSessionViewModel,navController: NavController){
    val sessionId by viewModel.sessionId.collectAsState()
    val guestUrl by viewModel.guestUrl.collectAsState()
    val playlist by viewModel.playlist.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSessionActive by viewModel.isSessionActive.collectAsState()

    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var screenState by remember { mutableStateOf(ScreenState.LINK) }


    //テストでUI開いたらコネクト
    LaunchedEffect(Unit){
        viewModel.connectToServer()
    }


    Box(modifier = Modifier
        .fillMaxSize()){

        when(screenState) {
            ScreenState.LINK -> {
                SessionLinkScreen(navController,viewModel)
            }

            ScreenState.PLAYLIST -> {
                SessionPlaylistScreen(navController,viewModel)
            }

            ScreenState.SEARCH -> {
                SessionRequestScreen(navController,viewModel)
            }

            else -> {}

        }

        SessionFooterNav(
            onChangeState = { newState ->
                screenState = newState
            }
        )
    }

}