package com.example.jambubble_client.ui.screens.auths

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jambubble_client.ui.components.buttons.RowTextButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.pannels.AuthBgPanel
import com.example.jambubble_client.ui.components.typographies.ConfirmLabel
import com.example.jambubble_client.ui.viewmodel.auths.RegisterConfirmUiState
import com.example.jambubble_client.ui.viewmodel.auths.RegisterConfirmViewModelFactory
import com.example.jambubble_client.ui.viewmodel.auths.RegisterConfirmViewmodel
import com.example.jambubble_client.ui.viewmodel.auths.RegisterViewModel
import java.time.format.DateTimeFormatter

@Composable
fun RegisterConfirmScreen(
    navController: NavController,
    viewModel: RegisterViewModel,
    confirmViewModel: RegisterConfirmViewmodel = viewModel(
        factory = RegisterConfirmViewModelFactory(
            LocalContext.current
        )
    )
) {

    //誕生日を表示用にstringに変換するおん
    val birthdayText = viewModel.birthday?.format(
        DateTimeFormatter.ofPattern("yyyy/MM/dd")
    ) ?: ""
    val state = confirmViewModel.uiState

    when(state){
        RegisterConfirmUiState.Success -> {
            LaunchedEffect(Unit) {
                navController.navigate("auth/register/confirm/email")
            }
        }
        RegisterConfirmUiState.Loading -> {
            CircularProgressIndicator()
        }

        is RegisterConfirmUiState.Error -> {
            Text(
                text = state.message,
                color = Color.Red
            )
        }
        else -> Unit
    }

    AuthBgPanel {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---------- アイコン（143×143） ----------
            Image(
                painter = rememberAsyncImagePainter(viewModel.imageUri),
                contentDescription = null,
                modifier = Modifier.size(143.dp)
            )

            // ---------- タイトル ----------
            Text(
                text = "確認画面",
                color = Color.White,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )

            // ---------- 各項目 ----------

            ConfirmLabel(label = "アカウント名", value = viewModel.name)
            ConfirmLabel(label = "メールアドレス", value = viewModel.email)
            ConfirmLabel(label = "パスワード", value = "・・・・")
            ConfirmLabel(label = "性別", value = viewModel.gender)
            ConfirmLabel(label = "生年月日", value = birthdayText)

            // ---------- 画像 ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            ) {
                Text(
                    text = "プロフィール画像",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .border(1.dp, Color.White, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(viewModel.imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ---------- ボタン ----------
            Submit(
                label = "確定",
                onClick = {
                    confirmViewModel.register(
                        name = viewModel.name,
                        email = viewModel.email,
                        password = viewModel.password,
                        gender = viewModel.gender,
                        birthday = birthdayText,
                        userImage = viewModel.imageUri
                    )
                }
            )


            RowTextButton(text = "もどる", onClick = {navController.navigate("")}, underline = true)
        }
    }
}
