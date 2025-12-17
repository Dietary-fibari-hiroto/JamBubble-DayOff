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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.spotifyremote.data.model.PlayerState
import com.example.jambubble_client.ui.viewmodel.musics.MusicPannelViewModel

@Composable
fun MusicPanelScreen(
    navController: NavController,
    viewModel: MusicPannelViewModel

) {
    //UI状態を監視
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        if(!uiState.isConnected){
            viewModel.connectToSpotify()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if(uiState.isConnected) {
            // 背景画像
            Image(
                painter = painterResource(id = R.drawable.offn),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.8f)
                    .blur(15.dp),
                contentScale = ContentScale.Crop
            )

            // ぼかし
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(15.dp)
            )

            // コンテンツ
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
                    .zIndex(5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ▼（下方向アイコン）
                Image(
                    painter = painterResource(id = R.drawable.chevron_down),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(vertical = 50.dp)
                        .size(40.dp)
                        .graphicsLayer {
                            rotationZ = 180f
                        }
                )

                // 3点リーダー
                Row(
                    modifier = Modifier
                        .width(300.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ellipsis),
                        contentDescription = "menu"
                    )
                }

                Spacer(Modifier.height(20.dp))

                // アルバム画像
                Image(
                    painter = painterResource(id = R.drawable.offn),
                    contentDescription = "album",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(Modifier.height(20.dp))

                // 曲情報
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text=uiState.playerState.trackName,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Text(
                            uiState.playerState.artistName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = "heart",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 30.dp)
                            .size(16.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                // シャッフル & リピートボタン
                Row(
                    modifier = Modifier
                        .width(300.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.shuffle),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                            .clickable{viewModel.toggleShuffle()}
                            .alpha(if(uiState.playerState.isShuffling)0.5f else 1f)
                    )
                    Spacer(Modifier.width(20.dp))
                    Image(
                        painter = painterResource(id =
                            when(uiState.playerState.repeatMode) {
                                PlayerState.RepeatMode.TRACK -> R.drawable.repeat_1
                                else -> R.drawable.repeat
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                            .clickable{viewModel.toggleRepeat()}
                            .alpha(if(uiState.playerState.repeatMode==PlayerState.RepeatMode.OFF)0.5f else 1f))
                }

                Spacer(Modifier.height(30.dp))

                // 時間バー
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .height(2.dp)
                            .background(Color.White)
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.width(300.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0:02", style = MaterialTheme.typography.bodySmall)
                        Text("-4:18", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(Modifier.height(60.dp))

                // 再生コントロール
                Row(
                    modifier = Modifier
                        .width(300.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.fast_forward),
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = 180f
                        }.size(48.dp)
                            .clickable{viewModel.skipPrevious()},
                    )

                    Image(
                        painter = painterResource(id = if(uiState.playerState.isPlaying)R.drawable.pause else R.drawable.play),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                            .clickable{viewModel.togglePlayPause()},
                    )

                    Image(
                        painter = painterResource(id = R.drawable.fast_forward),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                            .clickable{viewModel.skipNext()},
                    )
                }

                Spacer(Modifier.height(60.dp))
            }
        }else{
            Text(text="Spotifyへ接続できません。" + "お使いの環境にSpotifyアプリがダウンロードされ、ログインされているか確認してください")


            Button(
                onClick = { viewModel.connectToSpotify() },
                modifier = Modifier
                    .width(150.dp)
                    .height(60.dp)){
                Text(text="Spotifyへ接続する。")
            }

        }
    }
}


//テスト用
@Composable
fun ConnectionStatus(
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (isConnected)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.errorContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isConnected) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (isConnected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isConnected) "Connected 🎉" else "Disconnected",
                style = MaterialTheme.typography.titleMedium,
                color = if (isConnected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}
@Composable
private fun ConnectionButtons(
    isConnected: Boolean,
    isServiceBound: Boolean,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onConnectClick,
            modifier = Modifier.weight(1f),
            enabled = isServiceBound && !isConnected
        ) {
            Text("Connect")
        }

        OutlinedButton(
            onClick = onDisconnectClick,
            modifier = Modifier.weight(1f),
            enabled = isConnected
        ) {
            Text("Disconnect")
        }
    }
}