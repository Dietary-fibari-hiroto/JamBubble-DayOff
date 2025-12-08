package com.example.jambubble_client.ui.screens.auths

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.RowTextButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.inputs.CodeInputBox
import com.example.jambubble_client.ui.components.pannels.AuthBgPanel

@Composable
fun EmailConfirmScreen(
    navController: NavController,
    email: String = "example.email.com",

) {

    AuthBgPanel {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ロゴ画像
                Image(
                    painter = painterResource(id = R.drawable.jumbubblelogo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(143.dp)
                )

                Spacer(Modifier.height(20.dp))

                // タイトルテキスト
                Text(
                    text = "メールアドレスを確認してください",
                    fontSize = 18.sp,
                    color = Color.White
                )

                Spacer(Modifier.height(20.dp))

                // 説明ブロック
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = email,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "宛に確認コードが送信されました",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "メール受信箱を確認し、下記に認証コードを入力して\nあなたのメールアドレスを有効化認証してください。\nコードの有効期限まであと15:00です。",
                        fontSize = 12.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(30.dp))

                // 4桁入力ボックス
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(4) {
                        CodeInputBox()
                        Spacer(Modifier.width(10.dp))
                    }
                }

                Spacer(Modifier.height(30.dp))

                // 認証ボタン
                Submit(
                    label = "認証",
                    onClick = { navController.navigate("auth/register/confirm/email/complete") }
                )

                Spacer(Modifier.height(10.dp))

                RowTextButton(
                    text = "コードを再送",
                    onClick = { navController.navigate("") }
                )

                RowTextButton(
                    text = "最初に戻る",
                    onClick = { navController.navigate("entrance") }
                )
            }
        }

}
