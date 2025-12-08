package com.example.jambubble_client.ui.screens.auths

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jambubble_client.ui.components.elements.Bubble
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExplainScreen(navController: NavController) {

    var explainPanel by remember { mutableStateOf(true) }
    var explain2Panel by remember { mutableStateOf(false) }
    var babbleNext by remember { mutableStateOf(true) }

    var explainAlpha by remember { mutableFloatStateOf(1f) }
    var explain2Alpha by remember { mutableFloatStateOf(0f) }

    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                if (explain2Panel) {
                    navController.navigate("auth/register")
                } else {
                    // scope.launchを使って非同期処理を開始する
                    scope.launch { // ← 4. LaunchedEffectをscope.launchに変更
                        // Fade out 1st
                        babbleNext = false
                        explainAlpha = 0f

                        delay(1000) // 1秒待つ
                        explainPanel = false
                        explain2Panel = true

                        // Fade in 2nd
                        delay(10) // 少しだけ待ってから
                        explain2Alpha = 1f
                    }
                }
            }
    ) {

        // ---------- パネル１ ----------
        if (explainPanel) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x55000000))
                    .alpha(
                        animateFloatAsState(
                            targetValue = explainAlpha,
                            animationSpec = tween(1000)
                        ).value
                    )
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp),
                    text = """
                        ダウンロードしていただきありがとうございます！

                        このアプリは、みんなで一緒に音楽を作るための
                        プレイリストが作れる場所です。

                        使い方は自由ですが、マナーを守って楽しく安全に使いましょう！
                    """.trimIndent(),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ---------- パネル２ ----------
        if (explain2Panel) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(
                        animateFloatAsState(
                            targetValue = explain2Alpha,
                            animationSpec = tween(1000)
                        ).value
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "それではあなたのことを教えてください",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ---------- Bubble ----------
        Bubble(
            modifier = Modifier
                .absoluteOffset(x = 40.dp, y = 120.dp),
            next = babbleNext
        )
        Bubble(
            modifier = Modifier
                .absoluteOffset(x = 260.dp, y = 360.dp),
            next = babbleNext
        )
    }
}
