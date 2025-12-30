package com.example.jambubble_client.ui.components.cards

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jambubble_client.R
import com.example.jambubble_client.data.model.Guest
import com.example.jambubble_client.ui.components.buttons.MiniSubmit


@Composable
fun MemberCard(guest: Guest) {

    var isOpen by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(300.dp)
            .animateContentSize()  // ← 開閉アニメーション
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D2D2D) // var(--color-deep-gray)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- メイン行（タップで詳細開閉） ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isOpen = !isOpen }
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.offn),
                        contentDescription = "user",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(guest.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(guest.userId, fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Text("管理者", fontSize = 12.sp, color = Color.Gray)
            }

            // --- 開いたときの詳細コンテンツ ---
            if (isOpen) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "ゲストの権限設定",
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                PermissionSelectBox()

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    MiniSubmit(label = "確定", color = Color.Gray)
                    MiniSubmit(label = "退出させる", color = Color(0xFFFA2E5C))
                }
            }
        }
    }
}


@Composable
fun PermissionSelectBox() {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("test") }

    Box {
        Box(
            modifier = Modifier
                .width(250.dp)
                .height(45.dp)
                .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(selected, color = Color.White)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("test") },
                onClick = {
                    selected = "test"
                    expanded = false
                }
            )
        }
    }
}
