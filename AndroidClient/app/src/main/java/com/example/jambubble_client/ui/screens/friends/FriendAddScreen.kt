package com.example.jambubble_client.ui.screens.friends

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.inputs.SearchBar
import com.example.jambubble_client.ui.styles.ColorDeepGray
import com.example.jambubble_client.ui.styles.ColorSpotifyPrimary

enum class FriendRequestEnum {
    Default,
    SearchSuccess,
    Requested
}

@Composable
fun FriendAddPage(
    navController: NavController
) {
    var state by remember { mutableStateOf(FriendRequestEnum.Default) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        when (state) {

            // -------------------------------------------
            // DEFAULT
            // -------------------------------------------
            FriendRequestEnum.Default -> {
                DefaultContent(
                    onClose = {navController.navigate("app/friend")},
                )
            }

            // -------------------------------------------
            // SEARCH SUCCESS
            // -------------------------------------------
            FriendRequestEnum.SearchSuccess -> {
                SearchSuccessContent(
                    onRequest = { state = FriendRequestEnum.Requested }
                )
            }

            // -------------------------------------------
            // REQUESTED
            // -------------------------------------------
            FriendRequestEnum.Requested -> {
                RequestedContent(
                    onClose = {navController.navigate("app/friend")},
                    onRequest = { /* no-op */ }
                )
            }
        }
    }
}


@Composable
fun DefaultContent(
    onClose: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.8f)
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReturnButton(label = "", onClick = onClose)

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(placeholder = "IDで検索")
            Image(
                painter = painterResource(id = R.drawable.focus_black),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(Modifier.height(30.dp))

        Text("名前:ゆずき", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text("ID:132", fontSize = 18.sp, fontWeight = FontWeight.Bold,color = Color.Black)

        Spacer(Modifier.height(30.dp))

        Text("リクエスト用QRコード",color = Color.Black)
        Image(
            painter = painterResource(id = R.drawable.testqr),
            contentDescription = null,
            modifier = Modifier.size(186.dp)
        )
    }
}

@Composable
fun RequestedContent(
    onClose: () -> Unit,
    onRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.8f)
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("ユーザーが見つかりました！")

        Spacer(Modifier.height(50.dp))

        Image(
            painter = painterResource(id = R.drawable.circle_check_big),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )

        Spacer(Modifier.height(50.dp))

        Submit(
            label = "フレンドリクエスト",
            onClick = onRequest,
            backgroundColor= ColorSpotifyPrimary
        )

        Spacer(Modifier.height(30.dp))

        Submit(
            label = "閉じる",
            onClick = onClose,
            backgroundColor = ColorDeepGray
        )
    }
}


@Composable
fun SearchSuccessContent(
    onRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.8f)
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            SearchBar(placeholder = "IDで検索")
        }

        Spacer(Modifier.height(20.dp))

        Text("ユーザーが見つかりました！")

        Spacer(Modifier.height(20.dp))

        Text("名前:ゆずき", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("ID:132", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.offn),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(25.dp))
        )

        Spacer(Modifier.height(30.dp))

        Submit(
            label = "フレンドリクエスト",
            onClick = onRequest,
            backgroundColor=ColorSpotifyPrimary
        )
    }
}
