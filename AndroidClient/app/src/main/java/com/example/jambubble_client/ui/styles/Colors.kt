package com.example.jambubble_client.ui.styles


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ColorPrimary = Color(0xFFFF8500)
val ColorSecondary = Color(0xFF240046)
val ColorSpotifyPrimary = Color(0xFF1ED760)
val ColorSpotifySecondary = Color(0xFF147235)
val ColorUnselected = Color(0xFF1D1D1D)
val ColorSecondaryBg = Color(0xFF5A189A)
val ColorAppleMusic = Color(0xFFFA233B)
val ColorGray = Color(0xFFCCCCCC)
val ColorDeepGray = Color(0xFF555555)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val Typography = Typography(
        bodyLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.White // ← 追加
        ),
        bodyMedium = TextStyle(color = Color.White),
        bodySmall = TextStyle(color = Color.White),
        titleLarge = TextStyle(color = Color.White),
        titleMedium = TextStyle(color = Color.White),
        titleSmall = TextStyle(color = Color.White),
    )

    MaterialTheme(
        colorScheme = lightColorScheme(), // ← 白文字なら暗色テーマが合う
        typography = Typography,
        content = content
    )
}