package com.example.jambubble_client.ui.screens.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jambubble_client.R
import com.example.jambubble_client.data.model.PlaylistItem
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel

@Composable
fun SessionPlaylistScreen(navController: NavController,sessionViewModel: MusicSessionViewModel) {
    val playlist by sessionViewModel.playlist.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(playlist.isEmpty()){
            Text(
                text = "プレイリストは空です",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }else{
            LazyColumn{
                items(playlist.sortedBy { it.order }){ item ->
                    PlaylistItem(
                        item = item,
                        onRemove = { sessionViewModel.removeTrack(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MusicListLabel(
    title: String,
    artist: String,
    isPlay: Boolean = false,
    albumPainter: Painter = painterResource(id = R.drawable.movie), // 任意
    playIcon: Painter = painterResource(id = R.drawable.shell),
    menuIcon: Painter = painterResource(id = R.drawable.menu),
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(if (isPlay) 1f else 0.8f)
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 20.dp,
                bottom = if (isPlay) 20.dp else 0.dp
            )
            .border(1.dp, Color.White, RectangleShape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // ===== Left: Album (only when isPlay) =====
        if (isPlay) {
            Image(
                painter = albumPainter,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // ===== Center Text =====
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 10.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = artist,
                color = Color.LightGray,
                fontSize = 12.sp
            )
        }

        // ===== Right-side Icon =====
        Image(
            painter = if (isPlay) playIcon else menuIcon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
fun PlaylistItem(
    item: PlaylistItem,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.albumImageUrl,
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.trackName,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.artistName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "リクエスト: ${item.requestedBy}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onRemove) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "削除",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}