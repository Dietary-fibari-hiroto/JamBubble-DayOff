package com.example.jambubble_client.ui.screens.sessions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.buttons.UserIcon
import com.example.jambubble_client.ui.components.cards.SessionCardBig

@Composable
fun SessionSearchScreen(navController: NavController) {

    // どちらのタブがアクティブか
    var isTagMode by remember { mutableStateOf(true) }
    var searchText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        // ---------------------
        // 上部：戻るボタン & ユーザーアイコン
        // ---------------------
        Row(Modifier.fillMaxWidth()){
            ReturnButton(label="",onClick = {navController.navigate("app/session/search/detail")})
            UserIcon(navController = navController)
        }


        // ---------------------
        // 検索エリア（固定）
        // ---------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp)
                .zIndex(5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // タブ切り替え
            Row(
                modifier = Modifier
                    .width(220.dp)
                    .height(30.dp)
                    .background(Color(0xFF222222), shape = RoundedCornerShape(10.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                ModeButton(
                    text = "タグで探す",
                    active = isTagMode,
                    modifier = Modifier.weight(1f),
                    onClick = { isTagMode = true }
                )

                ModeButton(
                    text = "名前で探す",
                    active = !isTagMode,
                    modifier = Modifier.weight(1f),
                    onClick = { isTagMode = false }
                )
            }

            Spacer(Modifier.height(20.dp))

            // 検索ボックス
            Row(
                modifier = Modifier
                    .width(250.dp)
                    .height(40.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                    .background(Color(0xFF111111), shape = RoundedCornerShape(10.dp))
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(10.dp))

                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "search",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(10.dp))

                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // ---------------------
        // セッション一覧
        // ---------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 250.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SessionCardBig()
            SessionCardBig()
            SessionCardBig()
        }
    }
}
@Composable
fun ModeButton(
    text: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(if (active) Color.White else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (active) Color.Black else Color.White,
            fontSize = 12.sp
        )
    }
}
