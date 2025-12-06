package com.example.jambubble_client.ui.components.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CodeInputBox() {
    var value by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .size(width = 40.dp, height = 50.dp)
            .border(1.dp, Color.White, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= 1 && it.all { c -> c.isDigit() }) value = it
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
