package com.example.jambubble_client.ui.screens.sessions

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.inputs.ImagePickerBox
import com.example.jambubble_client.ui.components.inputs.SelectBox
import com.example.jambubble_client.ui.components.inputs.TextInput
import com.example.jambubble_client.ui.screens.musics.ProviderSwitch
import com.example.jambubble_client.ui.styles.ColorSpotifyPrimary


class SessionCreateViewModel : ViewModel() {
    var sessionName by mutableStateOf("")
    var password by mutableStateOf("")
    var description by mutableStateOf("")
    var scene by mutableStateOf("")
    var genre by mutableStateOf("")
    var guest by mutableStateOf("")
    var publish by mutableStateOf("")

    var isApple by mutableStateOf(false)

    var imageUri by mutableStateOf<Uri?>(null)
}

@Composable
fun SessionCreateScreen(
    navController: NavController,
    vm: SessionCreateViewModel,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReturnButton(label="", onClick = { navController.popBackStack() })

        Spacer(Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            ImagePickerBox(
                label = "セッション画像",
                supplement = "推奨サイズ 1200x1200",
                imageUri = vm.imageUri,
                onPickClick = {
                    // TODO: 画像Picker
                }
            )

            Spacer(Modifier.height(20.dp))

            TextInput(
                label = "セッション名",
                value = vm.sessionName,
                onValueChange = { vm.sessionName = it }
            )

            TextInput(
                label = "パスワード",
                value = vm.password,
                onValueChange = { vm.password = it }
            )

            TextInput(
                label = "説明",
                value = vm.description,
                onValueChange = { vm.description = it }
            )

            SelectBox(
                label = "シーン設定",
                options = listOf("飲み会", "作業", "通学", "くつろぎ"),
                value = vm.scene,
                onValueSelected = { vm.scene = it }
            )

            SelectBox(
                label = "ジャンル設定",
                options = listOf("Rock", "Lofi", "Chill", "Jazz"),
                value = vm.genre,
                onValueSelected = { vm.genre = it }
            )

            SelectBox(
                label = "ゲスト権限設定",
                options = listOf("視聴のみ", "編集可能"),
                value = vm.guest,
                onValueSelected = { vm.guest = it }
            )

            SelectBox(
                label = "公開設定",
                options = listOf("公開", "非公開"),
                value = vm.publish,
                onValueSelected = { vm.publish = it }
            )

            Spacer(Modifier.height(20.dp))

            Text("プロバイダ", fontSize = 14.sp, color = Color.White)

            ProviderSwitch(
                isApple = vm.isApple,
                onSpotify = { vm.isApple = false },
                onAppleMusic = { vm.isApple = true }
            )

            Spacer(Modifier.height(40.dp))

            Submit(
                label = "次へ",
                backgroundColor = ColorSpotifyPrimary,
                onClick = {
                    navController.navigate("app/session/create/confirm")
                }
            )

            Spacer(Modifier.height(60.dp))
        }
    }
}
