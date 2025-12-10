package com.example.jambubble_client.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.jambubble_client.ui.screens.musics.MusicPanelScreen
import com.example.jambubble_client.ui.screens.musics.PlaylistDetailScreen
import com.example.jambubble_client.ui.screens.musics.PlaylistScreen
import com.example.jambubble_client.ui.screens.musics.SearchScreen
import com.example.jambubble_client.ui.screens.users.HelpScreen
import com.example.jambubble_client.ui.screens.users.MainScreen
import com.example.jambubble_client.ui.screens.users.SettingScreen
import com.example.jambubble_client.ui.screens.users.UserMenu
import com.example.jambubble_client.ui.screens.users.UserProfileScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "brandscreen",
        modifier = Modifier.fillMaxSize()
    ){
        composable("brandscreen"){ BrandScreen(navController)}
        composable("entrance/loading"){ LoadingScreen(navController)}
        composable("entrance"){ EntranceScreen(navController) }


        composable("auth/login"){ LoginScreen(navController) }
        composable("auth/register"){ RegisterScreen(navController) }
        composable("auth/explain"){ ExplainScreen(navController) }
        composable("auth/register/confirm"){ RegisterConfirmScreen(
            name = "ゆずき",
            email = "yuzu@email.com",
            password = "・・・・",
            gender = "男性",
            birthday = "2004/11/11",
            imageUrl = "https://dawn-waiting.com/static/media/dawn_cat_ani.863d6550f404cf074627.png",
            navController = navController
        )
        }
        composable("auth/register/confirm/email"){ EmailConfirmScreen(navController) }
        composable("auth/register/confirm/email/complete"){ EmailCompleteScreen(navController) }
        composable("auth/register/provider"){ ProviderConfirmScreen(navController) }
        composable("auth/register/complete"){RegisterCompleteScreen(navController)}


        composable("app/main"){MainScreen(navController)}
        composable("app/user/profile"){ UserProfileScreen(navController)}
        composable("app/user/menu"){ UserMenu(navController) }
        composable("app/user/setting"){ SettingScreen(navController) }
        composable("app/user/help"){ HelpScreen(navController) }

        composable("app/search"){ SearchScreen(navController) }
        composable("app/playlist"){ PlaylistScreen(navController) }
        composable("app/playlist/detail"){ PlaylistDetailScreen(navController) }
        composable("app/music/panel"){ MusicPanelScreen(navController)}
    }
}