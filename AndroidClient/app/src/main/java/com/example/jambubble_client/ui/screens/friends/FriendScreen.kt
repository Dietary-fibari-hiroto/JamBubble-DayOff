package com.example.jambubble_client.ui.screens.friends

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.UserIcon
import com.example.jambubble_client.ui.components.elements.FornowIcon
import com.example.jambubble_client.ui.components.inputs.SearchBar
import com.example.jambubble_client.ui.components.navs.PlaylistBar
import com.example.jambubble_client.ui.components.pannels.SessionDeck

@Composable
fun FriendScreen(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {

        // --- Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SearchBar(placeholder = "友達を検索")
            Spacer(modifier = Modifier.width(10.dp))
            UserIcon(navController = navController)
        }

        // --- Menu Icons ---
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FriendMenuButton(R.drawable.user_round_plus, onClick = {navController.navigate("app/friend/add")})
            FriendMenuButton(R.drawable.map_pinned)
            FriendMenuButton(R.drawable.mail)
            FriendMenuButton(R.drawable.message_circle_plus)
        }

        // --- ForNow ---
        SectionLabel("ForNow")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(10.dp)
        ) {
            FornowIcon({navController.navigate("app/friend/fornow")})
            Spacer(modifier = Modifier.width(10.dp))
            FornowIcon({navController.navigate("app/friend/fornow")})
            Spacer(modifier = Modifier.width(10.dp))
            FornowIcon({navController.navigate("app/friend/fornow")})
        }

        // --- Friend's Sessions ---
        SessionDeck(title = "フレンドのセッション")

        // --- Past public sessions ---
        LabelWithMoreButton(
            label = "過去の公開セッション",
            onMoreClick = {}
        )

        Column {
            PlaylistBar(title = "淡路遠征", provider = "spotify", userName = "ゆうと", date = "2002/02/02", onClick = {})
            PlaylistBar(title = "淡路遠征", provider = "spotify", userName = "ゆうと", date = "2002/02/02", onClick = {})
            PlaylistBar(title = "淡路遠征", provider = "spotify", userName = "ゆうと", date = "2002/02/02", onClick = {})

        }

        // --- Friends list ---
        SectionLabel("フレンド")


        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            NamePlate("笹山ゆり", "images/photos/offn.jpg")
            Spacer(modifier = Modifier.width(16.dp))
            NamePlate("笹山ゆり", "images/photos/offn.jpg")
        }
    }

    // --- FriendAddDialog（実装済み前提） ---

}


@Composable
fun FriendMenuButton(iconPath: Int, onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Image(
            painter = painterResource(iconPath),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun LabelWithMoreButton(label: String, onMoreClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onMoreClick() }
        ) {
            Text("もっとみる", fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            ArrowRightIcon()
        }
    }
}

@Composable
fun ArrowRightIcon() {
    Box(
        modifier = Modifier
            .size(width = 12.dp, height = 1.dp)
            .background(Color.White)
    )
}

@Composable
fun NamePlate(name: String, imgPath: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.offn),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(name, fontSize = 14.sp)
    }
}
