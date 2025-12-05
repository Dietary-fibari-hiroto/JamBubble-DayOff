package com.example.jambubble_client.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.jambubble_client.ui.screens.BrandScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jambubble_client.ui.screens.entrance.EntranceScreen
import com.example.jambubble_client.ui.screens.entrance.LoadingScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "brandscreen",
    ){
        composable("brandscreen"){ BrandScreen(navController)}
        composable("entrance/loading"){ LoadingScreen(navController)}
        composable("entrance"){ EntranceScreen(navController) }
    }
}