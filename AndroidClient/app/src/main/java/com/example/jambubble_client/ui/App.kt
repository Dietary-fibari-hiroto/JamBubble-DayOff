package com.example.jambubble_client.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jambubble_client.spotifyremote.data.repository.SpotifyAuthRepository
import com.example.jambubble_client.ui.components.navs.FloatingFooterNav
import com.example.jambubble_client.ui.components.navs.SessionFooterNav
import com.example.jambubble_client.ui.layouts.AuthLayout
import com.example.jambubble_client.ui.layouts.EntranceLayout
import com.example.jambubble_client.ui.layouts.MainLayout
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel

@Composable
fun App(
    musicPannelViewModel: MusicPannelViewModel,authManager: SpotifyAuthRepository
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
        LayoutType.Default -> AppNavHost(navController,musicPannelViewModel,authManager)

        LayoutType.Entrance -> EntranceLayout {
            AppNavHost(navController,musicPannelViewModel,authManager)
        }

        LayoutType.Auth -> AuthLayout {
            AppNavHost(navController,musicPannelViewModel,authManager)
        }

        LayoutType.App -> MainLayout(
            content = { AppNavHost(navController,musicPannelViewModel,authManager) },
            footer = { FloatingFooterNav(navController) }
        )
        LayoutType.Function -> MainLayout(
            content={AppNavHost(navController,musicPannelViewModel,authManager)},
            footer = {SessionFooterNav(navController)}
        )
    }
}

enum class LayoutType {
    Default,Entrance,Auth,App,Function
}