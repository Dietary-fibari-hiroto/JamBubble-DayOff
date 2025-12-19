package com.example.jambubble_client.ui.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jambubble_client.R
import com.example.jambubble_client.data.UserLocalDataStore
import com.example.jambubble_client.data.api.ApiConfig


@Composable
fun UserIcon(
    modifier: Modifier = Modifier,navController: NavController,
) {
    val user by UserLocalDataStore.userFlow.collectAsState(initial = null)

    var isDialogOpen by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        IconButton(
            onClick={ navController.navigate("app/user/menu")},
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f)),

        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model= ApiConfig.BASE_URL+user?.imgUrl,
                    placeholder =painterResource(R.drawable.dawn_cat),
                    error = painterResource(R.drawable.dawn_cat)
                ),// 画像差し替え
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }


    }
}
