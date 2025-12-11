package com.example.jambubble_client.ui.screens.users

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.dto.SessionCardDto
import com.example.jambubble_client.dto.favoriteSongDto
import com.example.jambubble_client.ui.components.buttons.UserIcon
import com.example.jambubble_client.ui.components.cards.SongCard
import com.example.jambubble_client.ui.components.pannels.SessionDeck


@Composable
fun MainScreen(
    navController: NavController
) {

    Column {

        // ヘッダー
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.jumbubblelogo),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Text(
                "JamBubble",
                style = MaterialTheme.typography.titleLarge, fontWeight = MaterialTheme.typography.titleLarge.fontWeight
            )
            UserIcon(
                navController = navController
            )
        }

        Spacer(Modifier.height(20.dp))

        // セッションデッキ
        SessionDeck(
            title = "人気の公開セッション",
            sessions = sampleSessions
        )

        SessionDeck(
            title = "フレンドのセッション",
            sessions = sampleSessions
        )

        // Favorite Song
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Favorite Song",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(3) {
                    SongCard(favoriteSongDto = favoriteSongDto(id = it, title = "title"))
                }
            }
        }
    }
}

val sampleSessions = listOf(
    SessionCardDto(
        id = "session_001",
        title = "秋の夜に合いそうな曲集めてます",
        thumbnailRes = R.drawable.offn,
        providerRes = R.drawable.spotify_icon
    ),
    SessionCardDto(
        id = "session_002",
        title = "まったりチルタイム用プレイリスト",
        thumbnailRes = R.drawable.offn,
        providerRes = R.drawable.spotify_icon
    ),
    SessionCardDto(
        id = "session_003",
        title = "作業効率爆上がりBGM特集",
        thumbnailRes = R.drawable.offn,
        providerRes = R.drawable.applemusic_icon
    ),
    SessionCardDto(
        id = "session_004",
        title = "ドライブで聴きたいエモい曲",
        thumbnailRes = R.drawable.offn,
        providerRes = R.drawable.spotify_icon
    ),
    SessionCardDto(
        id = "session_005",
        title = "雨の日にしっとり聴きたい音楽",
        thumbnailRes = R.drawable.offn,
        providerRes = R.drawable.spotify_icon
    )
)

