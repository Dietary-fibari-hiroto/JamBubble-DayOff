package com.example.jambubble_client.ui.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextInput(
    label: String,
    supplement: String = "" ,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(vertical = 8.dp)) {

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White
        )

        Spacer(Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            placeholder = {
                Text("example")
            }
        )

        if (supplement.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = supplement,
                color = Color(0xFF888888),
                fontSize = 12.sp
            )
        }
    }
}

