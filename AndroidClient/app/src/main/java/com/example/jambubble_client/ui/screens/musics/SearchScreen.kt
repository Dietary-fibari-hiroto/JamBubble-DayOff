package com.example.jambubble_client.ui.screens.musics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.jambubble_client.R
import com.example.jambubble_client.spotifyremote.data.model.Track
import com.example.jambubble_client.ui.components.buttons.UserIcon
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel
import com.example.jambubble_client.ui.viewmodel.searchs.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    musicPannelViewModel: MusicPannelViewModel,
    navController: NavController
) {
    val uiState by musicPannelViewModel.uiState.collectAsStateWithLifecycle()

    //もしSpotifyRemoteが切れていたら再接続
    LaunchedEffect(Unit) {
        if(!uiState.isConnected){
            musicPannelViewModel.connectToSpotify()
        }
    }




    var isApple by remember { mutableStateOf(false) }

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isApple)
                    Brush.verticalGradient(
                        listOf(Color.Black, Color(0xFF721420))
                    )
                else
                    Brush.verticalGradient(
                        listOf(Color.Black, Color(0xFF1DB954))
                    )
            )
    ) {
        // ===== 上部入力エリア =====
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                UserIcon(navController = navController)
            }

            Spacer(Modifier.height(20.dp))

            ProviderSwitch(
                isApple = isApple,
                onSpotify = { isApple = false },
                onAppleMusic = { isApple = true }
            )
        }




        // ===== 下部スクロールエリア =====
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            if(isApple){
                Spacer(Modifier.height(20.dp))
            }else{
                when {
                    isLoading -> {
                        LoadingContent()
                    }
                    errorMessage != null -> {
                        ErrorContent(
                            message = errorMessage!!,
                            onDismiss = { viewModel.clearError() }
                        )
                    }
                    searchResults.isEmpty() && searchQuery.isNotBlank() -> {
                        EmptyContent()
                    }
                    searchResults.isNotEmpty() -> {
                        SearchResults(
                            tracks = searchResults,
                            musicPannelViewModel = musicPannelViewModel
                        )
                    }
                    else -> {
                        InitialContent()
                    }
                }
            }
        }
    }
}


@Composable
fun ProviderButton(
    imageRes: Int,
    active: Boolean,
    onClick: () -> Unit
) {
    // 非アクティブ → グレースケール
    val colorMatrix = if (active) null else ColorMatrix().apply {
        setToSaturation(0f)
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .background(
                if (active) Color.White.copy(alpha = 0.1f)
                else Color.Transparent
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            colorFilter = colorMatrix?.let { ColorFilter.colorMatrix(it) }
        )
    }
}
@Composable
fun ProviderSwitch(
    isApple: Boolean,
    onSpotify: () -> Unit,
    onAppleMusic: () -> Unit
) {

    val bgColor = if (isApple) Color(0xFF721420) else Color(0xFF1DB954)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .size(width = 150.dp, height = 50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(bgColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            ProviderButton(
                imageRes = R.drawable.spotify_icon,
                active = !isApple,
                onClick = onSpotify
            )

            ProviderButton(
                imageRes = R.drawable.applemusic_icon,
                active = isApple,
                onClick = onAppleMusic
            )
        }
    }
}

@Composable
public fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("音楽を検索...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
public fun SearchResults(
    tracks: List<Track>,
    musicPannelViewModel: MusicPannelViewModel
) {
    fun onTrackClick(trackUri: String) {
        musicPannelViewModel.playTrack(trackUri)
    }

    LazyColumn {
        items(tracks) { track ->
            TrackItem(
                track = track,
                onClick = { onTrackClick(track.uri) }
            )
        }
    }
}

@Composable
public fun TrackItem(
    track: Track,
    onClick: () -> Unit
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

            // 再生時間
            Text(
                text = track.durationFormatted,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
public fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
public fun ErrorContent(
    message: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
public fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No results found",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
public fun InitialContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "今日は、どんな曲にする？",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}