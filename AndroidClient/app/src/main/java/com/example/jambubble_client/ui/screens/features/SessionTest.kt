package com.example.jambubble_client.ui.screens.features


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jambubble_client.data.model.PlaylistItem
import com.example.jambubble_client.data.model.SpotifyTrackSearchResult
import com.example.jambubble_client.ui.viewmodel.musics.MusicSessionViewModel
import com.example.jambubble_client.ui.viewmodel.searchs.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionTest(viewModel: MusicSessionViewModel,searchViewModel: SearchViewModel) {
    val sessionId by viewModel.sessionId.collectAsState()
    val guestUrl by viewModel.guestUrl.collectAsState()
    val playlist by viewModel.playlist.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isSessionActive by viewModel.isSessionActive.collectAsState()

    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.connectToServer()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Music Session Host") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            if (isSessionActive) {
                FloatingActionButton(onClick = { showSearchDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "曲を追加")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            //エラーメッセージ表示
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { viewModel.clearError() }) {
                            Icon(Icons.Default.Close, contentDescription = "閉じる")
                        }
                    }
                }
            }

            if (!isSessionActive) {
                //セッション未開始状態
                SessionStartCard(
                    onStartSession = { deviceId ->
                        viewModel.createSession(deviceId)
                    },
                    isLoading = isLoading
                )
            } else {
                //セッション情報表示
                SessionInfoCard(
                    sessionId = sessionId ?: "",
                    guestUrl = guestUrl ?: "",
                    onCloseSession = { viewModel.closeSession() }//セッション終了トリガー
                )

                Spacer(modifier = Modifier.height(16.dp))

                //プレイリスト表示
                PlaylistSection(
                    playlist = playlist,
                    onRemoveTrack = { itemId -> viewModel.removeTrack(itemId) },
                    onReorderPlaylist = { orderedIds -> viewModel.reorderPlaylist(orderedIds) }
                )

                //TODO: Spotify Remote SDK統合
                //SpotifyPlayerSection(playlist)
            }
        }

        // 検索ダイアログ
        if (showSearchDialog) {
            SearchDialog(
                searchQuery = searchQuery,
                searchResults = searchResults,
                isLoading = isLoading,
                onSearchQueryChange = { searchQuery = it },
                onSearch = { searchViewModel.onSearchQueryChange(searchQuery) },
                onAddTrack = { track ->
                    viewModel.addTrack(track)
                    showSearchDialog = false
                    searchQuery = ""
                    viewModel.clearSearchResults()
                },
                onDismiss = {
                    showSearchDialog = false
                    searchQuery = ""
                    viewModel.clearSearchResults()
                }
            )
        }
    }
}

@Composable
fun SessionStartCard(
    onStartSession: (String) -> Unit,
    isLoading: Boolean
) {
    var deviceId by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "新しいセッションを開始",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "セッションを開始すると、ゲストが参加できるURLが生成されます",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = deviceId,
                onValueChange = { deviceId = it },
                label = { Text("Spotify Device ID") },
                placeholder = { Text("Optional") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onStartSession(deviceId) },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("セッションを開始")
                }
            }
        }
    }
}

@Composable
fun SessionInfoCard(
    sessionId: String,
    guestUrl: String,
    onCloseSession: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "セッションID: $sessionId",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onCloseSession) {
                    Icon(Icons.Default.Close, contentDescription = "セッション終了")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ゲストURL:",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = guestUrl,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PlaylistSection(
    playlist: List<PlaylistItem>,
    onRemoveTrack: (String) -> Unit,
    onReorderPlaylist: (List<String>) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "プレイリスト (${playlist.size}曲)",
                    style = MaterialTheme.typography.titleMedium
                )
                //TODO: 並び替えボタン実装
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (playlist.isEmpty()) {
                Text(
                    text = "プレイリストは空です",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                LazyColumn {
                    items(playlist.sortedBy { it.order }) { item ->
                        PlaylistItemRow(
                            item = item,
                            onRemove = { onRemoveTrack(item.id) }
                        )
                        if (item != playlist.last()) {
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistItemRow(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDialog(
    searchQuery: String,
    searchResults: List<SpotifyTrackSearchResult>,
    isLoading: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onAddTrack: (SpotifyTrackSearchResult) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("曲を検索") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    label = { Text("曲名、アーティスト名") },
                    trailingIcon = {
                        IconButton(onClick = onSearch) {
                            Icon(Icons.Default.Search, contentDescription = "検索")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (searchResults.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                        items(searchResults) { track ->
                            SearchResultItem(
                                track = track,
                                onAdd = { onAddTrack(track) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("閉じる")
            }
        }
    )
}

@Composable
fun SearchResultItem(
    track: SpotifyTrackSearchResult,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.albumImageUrl,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = onAdd) {
            Icon(Icons.Default.Add, contentDescription = "追加")
        }
    }
}

// TODO: Spotify Remote SDK統合
// @Composable
// fun SpotifyPlayerSection(playlist: List<PlaylistItem>) {
//     Card(modifier = Modifier.fillMaxWidth()) {
//         Column(modifier = Modifier.padding(16.dp)) {
//             Text(
//                 text = "Spotify Player",
//                 style = MaterialTheme.typography.titleMedium
//             )
//             Spacer(modifier = Modifier.height(16.dp))
//             // TODO: Spotify Remote SDK統合
//             // - SpotifyAppRemote.connect()でSpotifyアプリと接続
//             // - playlistから楽曲を再生
//             // - 再生状態の表示と制御UI
//             Text("Spotify Remote SDK統合予定")
//         }
//     }
// }