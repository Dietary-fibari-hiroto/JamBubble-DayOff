package com.example.jambubble_client.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jambubble_client.ui.layouts.EntranceLayout

@Composable
fun App() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


    val layoutType = when {
        currentRoute == null -> LayoutType.Default
        currentRoute.startsWith("entrance")-> LayoutType.Entrance
        currentRoute.startsWith("app/")-> ""
        currentRoute.startsWith("auth/") -> ""
        else -> LayoutType.Default
    }

    when(layoutType){
        LayoutType.Default -> {AppNavHost(navController)}
        LayoutType.Entrance ->{
            EntranceLayout{
                AppNavHost(navController)
            }
        }
    }


    /**
     *     when (layoutType) {
     *         LayoutType.Default -> {
     *             DefaultLayout {
     *                 AppNavHost(navController)
     *             }
     *         }
     *         LayoutType.MainBg -> {
     *             MainBgLayout {
     *                 AppNavHost(navController)
     *             }
     *         }
     *         LayoutType.FullScreen -> {
     *             FullScreenLayout {
     *                 AppNavHost(navController)
     *             }
     *         }
     */
}
enum class LayoutType {
    Default,Entrance
}