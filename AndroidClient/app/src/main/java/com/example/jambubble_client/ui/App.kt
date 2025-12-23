package com.example.jambubble_client.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jambubble_client.ui.components.navs.FloatingFooterNav
import com.example.jambubble_client.ui.layouts.AuthLayout
import com.example.jambubble_client.ui.layouts.EntranceLayout
import com.example.jambubble_client.ui.layouts.MainLayout
import com.example.jambubble_client.ui.viewmodel.auths.SpotifyAuthViewModel
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel
import com.example.jambubble_client.ui.viewmodel.searchs.SearchViewModel

@Composable
fun App(
    musicPannelViewModel: MusicPannelViewModel,
    authViewModel: SpotifyAuthViewModel,
    searchViewModel: SearchViewModel,
    sessionViewModel: MusicSessionViewModel
    ) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val layoutType = when {
        currentRoute == null -> LayoutType.Default
        currentRoute.startsWith("entrance") -> LayoutType.Entrance
        currentRoute.startsWith("app/") -> LayoutType.App
        currentRoute.startsWith("auth/") -> LayoutType.Auth
        currentRoute.startsWith("function/")->LayoutType.Function
        else -> LayoutType.Default
    }

    when (layoutType) {
        LayoutType.Default -> AppNavHost(navController,musicPannelViewModel,authViewModel,searchViewModel,sessionViewModel)

        LayoutType.Entrance -> EntranceLayout {
            AppNavHost(navController,musicPannelViewModel,authViewModel,searchViewModel,sessionViewModel)
        }

        LayoutType.Auth -> AuthLayout {
            AppNavHost(navController,musicPannelViewModel,authViewModel,searchViewModel,sessionViewModel)
        }

        LayoutType.App -> MainLayout(
            content = { AppNavHost(navController,musicPannelViewModel,authViewModel,searchViewModel,sessionViewModel) },
            footer = { FloatingFooterNav(navController) }
        )
        LayoutType.Function -> MainLayout(
            content={AppNavHost(navController,musicPannelViewModel,authViewModel,searchViewModel,sessionViewModel)},
        )
    }
}

enum class LayoutType {
    Default,Entrance,Auth,App,Function
}