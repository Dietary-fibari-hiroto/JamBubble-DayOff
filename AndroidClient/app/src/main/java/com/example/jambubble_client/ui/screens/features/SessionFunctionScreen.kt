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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.navs.SessionFooterNav
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel
import com.example.jambubble_client.ui.viewmodel.searchs.SearchViewModel

//スクリーンの切替ENUM
enum class ScreenState {
    LINK,
    SEARCH,
    PLAYLIST,
    MEMBER
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionFunctionScreen(viewModel: MusicSessionViewModel,searchViewModel: SearchViewModel,navController: NavController,musicPannelViewModel: MusicPannelViewModel){
    //検索につかうステータス
    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by searchViewModel.searchResults.collectAsStateWithLifecycle()
    val errorMessage by searchViewModel.errorMessage.collectAsStateWithLifecycle()



    //セッションで使うデータを格納する変数群
    val sessionId by viewModel.sessionId.collectAsState()
    val guestUrl by viewModel.guestUrl.collectAsState()
    val playlist by viewModel.playlist.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSessionActive by viewModel.isSessionActive.collectAsState()

    var showSearchDialog by remember { mutableStateOf(false) }

    //スクリーンのルート管理
    var screenState by remember { mutableStateOf(ScreenState.LINK) }


    //テストでUI開いたらコネクト
    LaunchedEffect(Unit){
        viewModel.connectToServer()
    }


    Box(modifier = Modifier
        .fillMaxSize()){

        when(screenState) {
            ScreenState.LINK -> {
                SessionLinkScreen(navController,viewModel,
                    onChangeState = { newState ->
                        screenState = newState
                    }
                )
            }

            ScreenState.PLAYLIST -> {
                SessionPlaylistScreen(navController,viewModel)
            }

            ScreenState.SEARCH -> {
                SessionRequestScreen(
                    navController,
                    viewModel,
                    searchViewModel,
                    musicPannelViewModel,
                    isLoading,
                    searchQuery,
                    searchResults,
                    errorMessage
                )
            }
            ScreenState.MEMBER -> {
                SessionMemberScreen(navController,viewModel,
                    onChangeState = { newState ->
                        screenState = newState
                    })
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