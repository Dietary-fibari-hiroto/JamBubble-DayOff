package com.example.jambubble_client.ui.screens.friends

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jambubble_client.R
import com.example.jambubble_client.data.UserLocalDataStore
import com.example.jambubble_client.data.api.ApiConfig
import com.example.jambubble_client.data.dto.OtherUserProfileDto
import com.example.jambubble_client.ui.components.buttons.Submit
import com.example.jambubble_client.ui.components.inputs.SearchBar
import com.example.jambubble_client.ui.styles.ColorDeepGray
import com.example.jambubble_client.ui.styles.ColorSpotifyPrimary
import com.example.jambubble_client.ui.viewmodel.friends.FriendAddViewModel
import com.example.jambubble_client.ui.viewmodel.friends.FriendAddViewModelFactory
import com.example.jambubble_client.ui.viewmodel.friends.qr.CameraPermissionWrapper
import com.example.jambubble_client.ui.viewmodel.friends.qr.QrScannerView
import com.example.jambubble_client.ui.viewmodel.friends.qr.generateQrCode


enum class FriendRequestEnum {
    Default,
    QrScanner,
    SearchSuccess,
    Requested
}


@Composable
fun FriendAddPage(
    navController: NavController
) {

    //ユーザーの情報を読みこむ
    val user by UserLocalDataStore.userFlow.collectAsState(initial = null)
    //VIewModelの初期化
    val viewModel: FriendAddViewModel = viewModel(
        factory = FriendAddViewModelFactory(LocalContext.current)
    )
    val friendState by viewModel.friendState.collectAsState()

    var scanned by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(FriendRequestEnum.Default) }
    val result by viewModel.qrResult.collectAsState()

    LaunchedEffect(result,state) {
        if (result != null) {
            state = FriendRequestEnum.SearchSuccess
            val friendId = result!!.toInt()
            viewModel.loadUserById(friendId)
            Log.d("TAG", "フレンド情報: $friendState")
        }
        if (state == FriendRequestEnum.QrScanner) {
            scanned = false
        }
    }

    //自分のIDを用いてQR生成
    val bitmap = remember{
        generateQrCode("84")
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column (modifier = Modifier
            .align(Alignment.TopStart)
            .padding(20.dp)
            .size(60.dp)
            .zIndex(5f)
            .clip(CircleShape)
            .background(Color.White),
            Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ){
            Icon(
                painter = painterResource(id = R.drawable.return_img),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clickable{navController.navigate("app/friend")}

            )
        }
        when (state) {

            
            FriendRequestEnum.Default -> {
                DefaultContent(
                    openScanner={state = FriendRequestEnum.QrScanner},
                    myQrCode = bitmap.asImageBitmap()
                )
            }
            FriendRequestEnum.QrScanner -> {
                CameraPermissionWrapper {

                    QrScannerView(
                        onQrDetected = {
                            if (!scanned) {
                                scanned = true
                                viewModel.onQrScanned(it)
                            }
                        }
                    )
                }
            }


            FriendRequestEnum.SearchSuccess -> {
                SearchSuccessContent(
                    onRequest = { state = FriendRequestEnum.Requested },
                    id = result,
                    onTurnBack = {
                        scanned = false
                        viewModel.clearResult()
                        state = FriendRequestEnum.Default
                    },
                    friendState = friendState
                )
            }

            FriendRequestEnum.Requested -> {
                RequestedContent(
                    onClose = {navController.navigate("app/friend")},
                    onRequest = { /* no-op */ }
                )
            }
        }





    }
}


@Composable
fun DefaultContent(
    myQrCode: ImageBitmap,
    openScanner: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.8f)
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(placeholder = "IDで検索")
            Image(
                painter = painterResource(id = R.drawable.focus_black),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
                    .clickable{openScanner()}
            )
        }

        Spacer(Modifier.height(30.dp))

        Text("名前:ゆずき", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text("ID:132", fontSize = 18.sp, fontWeight = FontWeight.Bold,color = Color.Black)

        Spacer(Modifier.height(30.dp))

        Text("リクエスト用QRコード",color = Color.Black)
        Image(
            bitmap = myQrCode,
            contentDescription = null,
            modifier = Modifier.size(186.dp)
        )
    }
}

@Composable
fun RequestedContent(
    onClose: () -> Unit,
    onRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.8f)
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("ユーザーが見つかりました！")

        Spacer(Modifier.height(50.dp))

        Image(
            painter = painterResource(id = R.drawable.circle_check_big),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )

        Spacer(Modifier.height(50.dp))

        Submit(
            label = "フレンドリクエスト",
            onClick = onRequest,
            backgroundColor= ColorSpotifyPrimary
        )

        Spacer(Modifier.height(30.dp))

        Submit(
            label = "閉じる",
            onClick = onClose,
            backgroundColor = ColorDeepGray
        )
    }
}


@Composable
fun SearchSuccessContent(
    onRequest: () -> Unit,
    onTurnBack: () -> Unit,
    id:String? = "132",
    friendState: Result<OtherUserProfileDto>?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.8f)
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(friendState != null){

            Spacer(Modifier.height(60.dp))

            Text("ユーザーが見つかりました！",color = Color.Black)

            Spacer(Modifier.height(30.dp))

            Text("名前:${friendState.getOrNull()?.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("ID:${id}", fontSize = 18.sp, fontWeight = FontWeight.Bold,color = Color.Black)

            Spacer(Modifier.height(50.dp))

            Image(
                painter = rememberAsyncImagePainter(
                    model = ApiConfig.BASE_URL+friendState.getOrNull()?.imgUrl,
                    placeholder =painterResource(R.drawable.dawn_cat),
                    error = painterResource(R.drawable.dawn_cat)
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(25.dp))
            )

            Spacer(Modifier.height(50.dp))

            Submit(
                label = "フレンドリクエスト",
                onClick = onRequest,
                backgroundColor=ColorSpotifyPrimary
            )
        }else{
            Text(text="ユーザー情報の取得に失敗しました。",color = Color.Black)
        }

        Spacer(Modifier.height(10.dp))
        Submit(
            label = "検索に戻る",
            onClick = onTurnBack,
            backgroundColor=ColorDeepGray
        )
    }
}



