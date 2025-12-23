package com.example.jambubble_client.ui.components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.jambubble_client.ui.styles.ColorPrimary

@Composable
fun Submit(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color = ColorPrimary,
    textColor: Color = Color.White,
    iconRes: Int? =null,
    iconSize: Int = 35
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(300.dp)
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (iconRes != null) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize.dp)
                )
                Spacer(Modifier.width(20.dp))
            }

            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }

    }
}
