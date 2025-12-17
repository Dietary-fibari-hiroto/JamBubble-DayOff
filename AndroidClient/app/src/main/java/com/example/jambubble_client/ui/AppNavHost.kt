package com.example.jambubble_client.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jambubble_client.ui.screens.BrandScreen

import com.example.jambubble_client.ui.screens.auths.EmailCompleteScreen
import com.example.jambubble_client.ui.screens.auths.EmailConfirmScreen
import com.example.jambubble_client.ui.screens.auths.ExplainScreen
import com.example.jambubble_client.ui.screens.auths.LoginScreen
import com.example.jambubble_client.ui.screens.auths.ProviderConfirmScreen
import com.example.jambubble_client.ui.screens.auths.RegisterCompleteScreen
import com.example.jambubble_client.ui.screens.auths.RegisterConfirmScreen
import com.example.jambubble_client.ui.screens.auths.RegisterScreen
import com.example.jambubble_client.ui.screens.entrance.EntranceScreen
import com.example.jambubble_client.ui.screens.entrance.LoadingScreen
import com.example.jambubble_client.ui.screens.features.SessionLinkScreen
import com.example.jambubble_client.ui.screens.features.SessionLoadingScreen
import com.example.jambubble_client.ui.screens.features.SessionMemberScreen
import com.example.jambubble_client.ui.screens.features.SessionPlaylistScreen
import com.example.jambubble_client.ui.screens.features.SessionRequestBoardScreen
import com.example.jambubble_client.ui.screens.features.SessionRequestScreen
import com.example.jambubble_client.ui.screens.friends.FriendAddPage
import com.example.jambubble_client.ui.screens.friends.FriendScreen
import com.example.jambubble_client.ui.screens.musics.FornowScreen
import com.example.jambubble_client.ui.screens.musics.MusicPanelScreen
import com.example.jambubble_client.ui.screens.musics.PlaylistDetailScreen
import com.example.jambubble_client.ui.screens.musics.PlaylistScreen
import com.example.jambubble_client.ui.screens.musics.SearchScreen
import com.example.jambubble_client.ui.screens.sessions.SessionCreateConfirmScreen
import com.example.jambubble_client.ui.screens.sessions.SessionCreateScreen
import com.example.jambubble_client.ui.screens.sessions.SessionCreateViewModel
import com.example.jambubble_client.ui.screens.sessions.SessionListScreen
import com.example.jambubble_client.ui.screens.sessions.SessionScreen
import com.example.jambubble_client.ui.screens.sessions.SessionSearchScreen
import com.example.jambubble_client.ui.screens.users.HelpScreen
import com.example.jambubble_client.ui.screens.users.MainScreen
import com.example.jambubble_client.ui.screens.users.SettingScreen
import com.example.jambubble_client.ui.screens.users.UserMenu
import com.example.jambubble_client.ui.screens.users.UserProfileScreen
import com.example.jambubble_client.ui.viewmodel.auths.RegisterViewModel
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel

@Composable
fun AppNavHost(navController: NavHostController,musicPannelViewModel: MusicPannelViewModel) {

    val sessionVm: SessionCreateViewModel = viewModel()
    val userVm: RegisterViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "brandscreen",
    ) {
        composable("brandscreen") { BrandScreen(navController) }
        composable("entrance/loading") { LoadingScreen(navController) }
        composable("entrance") { EntranceScreen(navController) }


        composable("auth/login") { LoginScreen(navController) }
        composable("auth/register") { RegisterScreen(navController,userVm) }
        composable("auth/explain") { ExplainScreen(navController) }
        composable("auth/register/confirm") {
            RegisterConfirmScreen(navController = navController,viewModel = userVm)
        }
        composable("auth/register/confirm/email") { EmailConfirmScreen(navController) }
        composable("auth/register/confirm/email/complete") { EmailCompleteScreen(navController) }
        composable("auth/register/provider") { ProviderConfirmScreen(navController) }
        composable("auth/register/complete") { RegisterCompleteScreen(navController) }


        composable("app/main") { MainScreen(navController) }
        composable("app/user/profile") { UserProfileScreen(navController) }
        composable("app/user/menu") { UserMenu(navController) }
        composable("app/user/setting") { SettingScreen(navController) }
        composable("app/user/help") { HelpScreen(navController) }

        composable("app/search") { SearchScreen(navController) }
        composable("app/playlist") { PlaylistScreen(navController) }
        composable("app/playlist/detail") { PlaylistDetailScreen(navController) }
        composable("app/music/panel") { MusicPanelScreen(navController,musicPannelViewModel) }
        composable("app/session/search") { SessionSearchScreen(navController) }
        composable("app/session/List") { SessionListScreen(navController) }
        composable("app/friend") { FriendScreen(navController) }
        composable("app/friend/add") { FriendAddPage(navController) }
        composable("app/friend/user") { UserProfileScreen(navController) }
        composable("app/friend/fornow") { FornowScreen(navController) }

        composable("app/session") { SessionScreen(navController) }
        composable("app/session/create") { SessionCreateScreen(navController, sessionVm) }
        composable("app/session/create/confirm") {
            SessionCreateConfirmScreen(
                navController,
                sessionVm
            )
        }
        composable("session/loading"){SessionLoadingScreen(navController)}
        composable("function/session/link"){ SessionLinkScreen(navController) }
        composable("function/session/request"){SessionRequestScreen(navController)}
        composable("function/session/request/board"){SessionRequestBoardScreen(navController)}
        composable("function/session/playlist"){SessionPlaylistScreen(navController)}
        composable("function/session/member"){SessionMemberScreen(navController)}


    }
}