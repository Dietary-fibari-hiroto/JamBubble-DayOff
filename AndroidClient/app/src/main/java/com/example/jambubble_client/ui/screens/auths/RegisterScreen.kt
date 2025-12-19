package com.example.jambubble_client.ui.screens.auths

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.RowTextButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.inputs.BirthDatePicker
import com.example.jambubble_client.ui.components.inputs.ImagePickerBox
import com.example.jambubble_client.ui.components.inputs.SelectBox
import com.example.jambubble_client.ui.components.inputs.TextInput
import com.example.jambubble_client.ui.components.pannels.AuthBgPanel
import com.example.jambubble_client.ui.viewmodel.auths.RegisterViewModel


@Composable
fun RegisterScreen(
    navController: NavController,
    vm: RegisterViewModel
) {
    var passwordConfirm by remember { mutableStateOf("") }
    val genderOptions = listOf("男性", "女性", "どちらでもない")
    var isChecked by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        vm.imageUri = uri
        uri?.let {
            Log.d("Register", "選択画像: $it")
        }
    }

    AuthBgPanel {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 20.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---------- アイコン ----------
            Image(
                painter = painterResource(id = R.drawable.jumbubblelogo),
                contentDescription = null,
                modifier = Modifier.size(143.dp)
            )

            Text(
                text = "新規作成",
                color = Color.White,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ----------- 入力 UI -----------

            TextInput(
                label = "アカウント名",
                value = vm.name,
                onValueChange = { vm.name = it })
            TextInput(label = "メールアドレス", value = vm.email, onValueChange = { vm.email = it })
            TextInput(label = "パスワード", value = vm.password, onValueChange = { vm.password = it })
            TextInput(
                label = "パスワード（確認）",
                value = passwordConfirm,
                onValueChange = { passwordConfirm = it })

            SelectBox(
                label = "性別",
                options = genderOptions,
                value = vm.gender,
                onValueSelected = { vm.gender = it }
            )

            BirthDatePicker(
                label = "生年月日",
                value = vm.birthday,
                onValueChange = { vm.birthday = it }
            )

            ImagePickerBox(
                label = "プロフィール画像",
                supplement = "推奨サイズ: 300x300px",
                imageUri = vm.imageUri,
                onPickClick = { imagePickerLauncher.launch("image/*") }
            )

            // ---------- 規約 ----------
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Text(
                    text = "利用規約",
                    color = Color(0xFFFF9800),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = " に同意します",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "＊利用規約をお読みの上、チェックを入れてください",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))


            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Submit(label = "次へ", onClick = { navController.navigate("auth/register/confirm") })

                Spacer(Modifier.height(12.dp))

                RowTextButton(
                    text = "もどる",
                    underline = true,
                    onClick = { navController.navigate("entrance") }
                )
            }
        }
    }
}
