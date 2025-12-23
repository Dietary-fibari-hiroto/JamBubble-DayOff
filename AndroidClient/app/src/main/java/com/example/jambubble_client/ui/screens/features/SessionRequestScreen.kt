package com.example.jambubble_client.ui.screens.features

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jambubble_client.R
import com.example.jambubble_client.data.model.SpotifyTrackSearchResult
import com.example.jambubble_client.spotifyremote.data.model.Track
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.screens.musics.EmptyContent
import com.example.jambubble_client.ui.screens.musics.ErrorContent
import com.example.jambubble_client.ui.screens.musics.InitialContent
import com.example.jambubble_client.ui.screens.musics.LoadingContent
import com.example.jambubble_client.ui.screens.musics.SearchBar
import com.example.jambubble_client.ui.styles.ColorDeepGray
import com.example.jambubble_client.ui.styles.ColorSecondary
import com.example.jambubble_client.ui.styles.ColorSecondaryBg
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel
import com.example.jambubble_client.ui.viewmodel.searchs.SearchViewModel
import kotlinx.coroutines.delay

@Composable
fun SessionRequestScreen(
    navController: NavController,
    sessionViewModel: MusicSessionViewModel,
    searchViewModel: SearchViewModel,
    musicPannelViewModel: MusicPannelViewModel,
    isLoading: Boolean,
    searchQuery: String,
    searchResults: List<Track>,
    errorMessage: String?
) {


    var isLibrarySelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        // ===== タイトル =====
        Text(
            text = "リクエスト",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .width(220.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF444444)), // deep-gray想定
            verticalAlignment = Alignment.CenterVertically
        ) {

            //楽曲検索ボタン
            SwitchButton(
                text = "楽曲検索",
                isActive = !isLibrarySelected,
                onClick = { isLibrarySelected = false }
            )

            //マイライブラリボタン
            SwitchButton(
                text = "マイライブラリ",
                isActive = isLibrarySelected,
                onClick = { isLibrarySelected = true }
            )
        }

        Spacer(Modifier.height(20.dp))

        SearchBar(
            query = searchQuery,
            onQueryChange = { searchViewModel.onSearchQueryChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        when {
            isLoading -> {
                LoadingContent()
            }
            errorMessage != null -> {
                ErrorContent(
                    message = errorMessage!!,
                    onDismiss = { searchViewModel.clearError() }
                )
            }
            searchResults.isEmpty() && searchQuery.isNotBlank() -> {
                EmptyContent()
            }
            searchResults.isNotEmpty() -> {
                SearchResults(
                    tracks = searchResults,
                    addTrack = {
                        track ->
                        sessionViewModel.addTrack(track.toSpotifySearchResult())
                    }
                )
            }
            else -> {
                InitialContent()
            }
        }
    }
}

@Composable
fun SwitchButton(
    text: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(110.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isActive) Color.White else Color.Transparent
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isActive) Color.Black else Color.White
        )
    }
}


@Composable
private fun SearchResults(
    tracks: List<Track>,
    addTrack: (Track) -> Unit,
) {
    var selectedTrack by remember { mutableStateOf<Track?>(null) }

    selectedTrack?.let { track ->
        TrackDetailDialog(
            track = track,
            onDismiss = { selectedTrack = null },
            addClick = {
                addTrack(track)
            }
        )
    }

    LazyColumn {
        items(tracks) { track ->
            TrackItem(
                track = track,
                onClick = {selectedTrack = track},
                addClick = {
                    addTrack(track)
                }
            )
        }
    }
}


@Composable
private fun TrackItem(
    track: Track,
    onClick: () -> Unit,
    addClick:()->Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        )

    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // アルバムアート
            AsyncImage(
                model = track.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // トラック情報
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = track.artistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            AddButton(onClick = addClick )
        }
    }
}

@Composable
fun AddButton(
    onClick: () -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }
    LaunchedEffect(isChecked) {
        if (isChecked) {
            delay(800)
            isChecked = false
        }
    }

    Button(
        onClick = {
            onClick()
            if (!isChecked) {
                isChecked = true
            }
        },
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .width(64.dp)
            .height(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorSecondary
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        AnimatedContent(
            targetState = isChecked,
            transitionSpec = {
                scaleIn() + fadeIn() togetherWith
                        scaleOut() + fadeOut()
            },
            label = "AddCheckAnimation"
        ) { checked ->
            if (checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White
                )
            } else {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}


//Trackを型変換する
fun Track.toSpotifySearchResult(): SpotifyTrackSearchResult {
    return SpotifyTrackSearchResult(
        id = this.id,
        name = this.name,
        artist = this.artistName,
        album = this.albumName,
        albumImageUrl = this.imageUrl ?: "",
        durationMs = this.durationMs.toInt()
    )
}


//楽曲の詳細表示ダイアログ
@Composable
fun TrackDetailDialog(
    track: Track,
    onDismiss: () -> Unit,
    addClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties
            (
        usePlatformDefaultWidth = false
                    )
    ) {

        Box(
            modifier = Modifier
                .width(screenWidth * 0.9f)
                .height(screenHeight * 0.8f)
                .padding(24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF121212)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                AsyncImage(
                    model = track.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(20.dp))

                Text(text = track.name, fontSize = 20.sp)
                Text(text = track.artistName, fontSize = 12.sp)
                Spacer(Modifier.height(10.dp))
                Text(text = "アルバム:"+track.albumName, fontSize = 12.sp)
                Text(text = "再生時間:"+track.durationFormatted, fontSize = 12.sp)


                if (track.explicit) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Explicit",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    Submit(
                        iconRes = R.drawable.arrow_up_from_line,
                        iconSize = 20,
                        label = "リクエスト",
                        onClick = {
                            addClick()
                            onDismiss()
                        },
                        backgroundColor = ColorSecondaryBg
                    )
                    Spacer(Modifier.height(10.dp))
                    Submit(
                        label = "閉じる",
                        onClick = onDismiss,
                        backgroundColor = ColorDeepGray
                    )
                }
            }
        }
    }
}


@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}

