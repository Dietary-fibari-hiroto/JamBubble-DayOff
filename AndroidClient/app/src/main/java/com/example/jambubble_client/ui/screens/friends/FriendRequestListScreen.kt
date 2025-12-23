package com.example.jambubble_client.ui.screens.friends

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jambubble_client.R


data class FriendRequest(
    val id: Int,
    val userName: String,
    val userIconUrl: String
)

val friendRequestList = listOf(
    FriendRequest(1, "たろう", "https://example.com/user1.png"),
    FriendRequest(2, "はなこ", "https://example.com/user2.png"),
    FriendRequest(3, "けんじ", "https://example.com/user3.png")
)

@Composable
fun FriendRequestListScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column (modifier = Modifier
            .padding(20.dp)
            .size(60.dp)
            .clip(CircleShape),
            Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.return_img),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable{navController.navigate("app/friend")}

            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {


            items(friendRequestList) { request ->
                FriendRequestItem(
                    request = request,
                    onAccept = {
                        // 承諾API呼ぶ想定
                    },
                    onReject = {
                        // 拒否API呼ぶ想定
                    }
                )
                Divider()
            }
        }
    }
}



@Composable
fun FriendRequestItem(
    request: FriendRequest,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ユーザーアイコン
        AsyncImage(
            model = request.userIconUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // テキスト
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = request.userName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "友達申請が届いています",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        // 承諾ボタン
        Button(
            onClick = onAccept,
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text("承諾")
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 拒否ボタン
        OutlinedButton(
            onClick = onReject,
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text("拒否")
        }
    }
}
