package com.example.jambubble_client.ui.screens.entrance

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.elements.Bubble
import com.example.jambubble_client.ui.components.elements.bottomBubble
import com.example.jambubble_client.ui.components.elements.topBubble
import com.example.jambubble_client.ui.styles.ColorPrimary

@Composable
@Preview
fun EntrancePreview(){
    val navController = rememberNavController() // ← ダミーとして使える
    EntranceScreen(navController)
}


@Composable
fun EntranceScreen (navController: NavController){
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.fillMaxSize().padding().align(Alignment.TopCenter), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text="JamBubble",style = MaterialTheme.typography.headlineLarge,fontWeight= FontWeight.Bold,color= Color.White)
                Text(text="へようこそ",style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(40.dp))

            Text(
                text = "アカウントを持ってる人はログイン！\nまだの人は、今すぐ作ってみよう！",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(80.dp))

            Column(verticalArrangement = Arrangement.spacedBy(20.dp)){
                Submit(label="ログイン",onClick = {navController.navigate("auth/login")}, backgroundColor = Color.Transparent, modifier = Modifier.border(1.dp,ColorPrimary,shape = RoundedCornerShape(10.dp)))
                Submit(label="アカウントを作る",onClick={navController.navigate("auth/explain")})
            }
        }
        Bubble(
            modifier = Modifier.topBubble(this)
        )
        Bubble(
            modifier = Modifier
                .bottomBubble(this)

        )
    }
}