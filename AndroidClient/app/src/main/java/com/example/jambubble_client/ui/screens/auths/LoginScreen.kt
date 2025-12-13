package com.example.jambubble_client.ui.screens.auths

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.RowTextButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.inputs.TextInput
import com.example.jambubble_client.ui.components.pannels.AuthBgPanel
import com.example.jambubble_client.ui.viewmodel.auths.LoginUiState
import com.example.jambubble_client.ui.viewmodel.auths.LoginViewModel
import com.example.jambubble_client.ui.viewmodel.auths.LoginViewModelFactory

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(context)
    )

    val state = viewModel.uiState




    var email by remember{mutableStateOf("")}
    var password  by remember{mutableStateOf("")}

        AuthBgPanel {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.jumbubblelogo),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(143.dp)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "ログイン",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White

            )

            when(state){
                LoginUiState.Loading -> CircularProgressIndicator()

                LoginUiState.Success ->{
                    LaunchedEffect(Unit) {
                        navController.navigate("app/main") {
                            popUpTo("auth/login") { inclusive = true }
                        }
                    }
                }
                is LoginUiState.Error -> {
                    Text(text = state.message, color = Color.Red)
                }
                else->Unit
            }


            Spacer(Modifier.height(20.dp))

            // ---- Input area ----
            Column(
                Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextInput(label = "メールアドレス",value=email,onValueChange = {email=it})
                Spacer(Modifier.height(16.dp))
                TextInput(
                    label = "パスワード",
                    supplement = "半角英数、数字を含む8文字以上",
                    value = password,
                    onValueChange = { password = it }
                )
            }

            Spacer(Modifier.height(30.dp))

            // ---- Buttons ----
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Submit(label = "次へ", onClick = { viewModel.login(email,password) })

                Spacer(Modifier.height(12.dp))

                RowTextButton(
                    text = "もどる",
                    underline = true,
                    onClick = { navController.navigate("entrance") }
                )
            }
        }
}
