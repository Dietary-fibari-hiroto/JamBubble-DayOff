package com.example.jambubble_client.ui.screens.sessions

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.typographies.ConfirmLabel
import com.example.jambubble_client.ui.styles.ColorSpotifyPrimary

@Composable
fun SessionCreateConfirmScreen(
    navController: NavController,
    vm: SessionCreateViewModel
) {
    val providerIcon = if (vm.isApple) {
        painterResource(R.drawable.applemusic_icon)
    } else {
        painterResource(R.drawable.spotify_icon)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top=40.dp, bottom = 100.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ReturnButton(label="", onClick = { navController.popBackStack() })

        Spacer(Modifier.height(40.dp))

        Box(
            modifier = Modifier.size(143.dp),
            contentAlignment = Alignment.Center
        ) {
            if (vm.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(if(vm.imageUri !=null) vm.imageUri else Uri.parse("https://dawn-waiting.com/static/media/dawn_cat.95f7a64e177a4125d31c.png")),
                    contentDescription = null,
                    modifier = Modifier
                        .size(143.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        ConfirmLabel("セッション名", vm.sessionName)
        ConfirmLabel("パスワード", vm.password)
        ConfirmLabel("シーン設定", vm.scene)
        ConfirmLabel("ジャンル設定", vm.genre)
        ConfirmLabel("ゲスト権限設定", vm.guest)
        ConfirmLabel("公開設定", vm.publish)

        Spacer(Modifier.height(20.dp))

        ProviderConfirm(providerIcon)

        Spacer(Modifier.height(40.dp))

        Submit(backgroundColor = ColorSpotifyPrimary,label = "セッションを始める", onClick = {
            navController.navigate("session/loading")
        })
    }
}

@Composable
fun ProviderConfirm(icon: Painter) {
    Column(
        modifier = Modifier
            .width(300.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "プロバイダ",
            color = Color.White,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(8.dp))

        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
        )
    }
}

