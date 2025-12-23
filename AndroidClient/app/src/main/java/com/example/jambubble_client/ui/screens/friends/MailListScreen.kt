package com.example.jambubble_client.ui.screens.friends

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
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
import com.example.jambubble_client.R

data class Notice(
    val id: Int,
    val title: String,
    val message: String,
    val date: String
)



@Composable
fun MailListScreen(navController: NavController){
    val noticeList = listOf(
        Notice(1, "ゆみから友達申請が届いています。", "本日23:00よりシステムメンテナンスを行います。", "2025/12/19"),
        Notice(2, "新機能追加", "プロフィール編集機能を追加しました。", "2025/12/18"),
        Notice(3, "不具合修正", "一部端末で発生していた不具合を修正しました。", "2025/12/17")
    )
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
            items(noticeList) { notice ->
                NoticeItem(notice)
                Divider()
            }
        }
    }
}
@Composable
fun NoticeItem(notice: Notice) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = notice.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = notice.message,
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = notice.date,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
