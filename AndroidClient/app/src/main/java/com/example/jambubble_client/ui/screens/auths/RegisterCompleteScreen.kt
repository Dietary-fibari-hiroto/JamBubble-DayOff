package com.example.jambubble_client.ui.screens.auths

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.RowTextButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.pannels.AuthBgPanel
import com.example.jambubble_client.ui.viewmodel.auths.InitialLoadingViewModelFactory
import com.example.jambubble_client.ui.viewmodel.auths.InitialLoginState
import com.example.jambubble_client.ui.viewmodel.auths.InitialLoginViewModel

@Composable
fun RegisterCompleteScreen(
    navController: NavController
) {
    //登録した情報をもとに初回だけ自動ログインする処理
    val context = LocalContext.current

    val viewModel: InitialLoginViewModel = viewModel(
        factory = InitialLoadingViewModelFactory(context)
    )

    val state = viewModel.uiState




    AuthBgPanel {

        // ロゴ
        Image(
            painter = painterResource(id = R.drawable.jumbubblelogo),
            contentDescription = null,
            modifier = Modifier
                .size(143.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(25.dp))

            when(state){
                InitialLoginState.Idle -> {

                    // タイトル
                    Text(
                        text = "アカウントを作成しました",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    // 説明文
                    Text(
                        text = "ようこそJumBubbleへ\n" +
                                "ここから始まるのは、みんなの“好き”が集まった時間。\n" +
                                "曲を追加して、一緒にプレイリストを作っていきましょう。\n",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    // ボタン
                    Column(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Submit(
                            label = "はじめる",
                            onClick = { viewModel.initialLogin() },
                        )
                    }
                }

                InitialLoginState.Loading -> CircularProgressIndicator()

                InitialLoginState.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("app/main") {
                            popUpTo("auth/register") { inclusive = true }
                        }

                    }
                }
                is InitialLoginState.Error->{
                    Text(text="ログイン処理失敗しました。時間をおいてから再度お試しください。"+state.message, color = Color.Red)
                    RowTextButton(
                        text = "もどる",
                        underline = true,
                        onClick = { navController.navigate("entrance") }
                    )
                }

                else -> Unit
            }

        }
    }
}
