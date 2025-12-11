package com.example.jambubble_client.ui.screens.friends

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.R
import com.example.jambubble_client.ui.components.buttons.MiniSubmit
import com.example.jambubble_client.ui.components.buttons.ReturnButton
import com.example.jambubble_client.ui.layouts.BlurPanel
import com.example.jambubble_client.ui.screens.users.SectionFrame
import com.example.jambubble_client.ui.styles.ColorAppleMusic
import com.example.jambubble_client.ui.styles.ColorDeepGray
import com.example.jambubble_client.ui.styles.ColorPrimary

@Composable
fun ProfileScreen(
    navController: NavController
) {
    var process by remember { mutableStateOf(Process.Default) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // --- Main Content ---
        ProfileContent(
            onOpenDialog = { process = Process.IsOpen },
            modifier = Modifier.fillMaxSize()
        )

        // --- Dialog Layer ---
        if (process != Process.Default) {
            BlurPanel {
                DialogContainer(
                    process = process,
                    onClose = { process = Process.Default },
                    onBlock = { process = Process.IsBlock },
                    onRequest = { process = Process.IsRequest }
                )
            }
        }
    }
}

enum class Process {
    Default,
    IsOpen,
    IsBlock,
    IsRequest
}



@Composable
fun ProfileContent(
    onOpenDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ReturnButton(label = "Profile", onClick = {})

        Box(
            Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                painter = painterResource(R.drawable.ellipsis),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onOpenDialog() }
            )
        }

        // Profile Photo
        Box(
            modifier = Modifier
                .size(200.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.offn),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }

        Spacer(Modifier.height(10.dp))

        Text("ゆずき", style = MaterialTheme.typography.titleMedium)

        Text(
            text = "ローファイとシティポップが日常のBGM。\n朝はコーヒーとゆるめのビート、夜はギターが心地いい曲を。\n最近はインディ系を掘るのがマイブーム。",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Favorite Song
        SectionFrame(title = "Favorite song") {
            Box(
                modifier = Modifier
                    .size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.movie),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                )
            }

            Text("Movie", style = MaterialTheme.typography.titleSmall)
            Text("Tom Misch・Spotify", style = MaterialTheme.typography.bodySmall)
        }

        MiniSubmit(label = "保存", color = ColorPrimary)

        // History
        SectionFrame(title = "過去の履歴") {
            Text("セッション数", style = MaterialTheme.typography.bodySmall)
            Row(verticalAlignment = Alignment.Bottom) {
                Text("12", style = MaterialTheme.typography.titleLarge)
                Text("回", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(50.dp))
    }
}
@Composable
fun SectionFrame(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(Modifier.height(8.dp))

        content()
    }
}


@Composable
fun DialogContainer(
    process: Process,
    onClose: () -> Unit,
    onBlock: () -> Unit,
    onRequest: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxWidth(0.9f)
            .background(Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(15.dp))
            .padding(vertical = 30.dp)
            .blur(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ReturnButton(onClick = onClose, label="")

        when (process) {
            Process.IsOpen -> {
                MenuItem(
                    icon = R.drawable.shield_ban,
                    text = "ユーザーをブロック",
                    onClick = onBlock
                )
                MenuItem(
                    icon = R.drawable.user_round_plus,
                    text = "フレンド申請",
                    onClick = onRequest
                )
            }

            Process.IsBlock -> {
                MenuItem(
                    icon = R.drawable.shield_ban,
                    text = "ユーザーをブロック"
                )

                Text(
                    "ゆずきをブロックしてよろしいでしょうか？",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiniSubmit(label = "いいえ", color = ColorDeepGray)
                    MiniSubmit(label = "ブロック", color= ColorAppleMusic)
                }
            }

            else -> {}
        }
    }
}


@Composable
fun MenuItem(
    icon: Int,
    text: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Black
        )
        Spacer(Modifier.width(10.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}
