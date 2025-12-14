package com.example.jambubble_client.data.dto

import androidx.annotation.DrawableRes
import com.example.jambubble_client.R


data class SessionCardDto(
    val id: String, // 各セッションを区別するための一意なID
    val title: String,
    @DrawableRes val thumbnailRes: Int, // サムネイル画像のリソースID
    @DrawableRes val providerRes: Int   // プロバイダーアイコンのリソースID
)

data class favoriteSongDto(
    val id:Int,
    val title: String,
    @DrawableRes val thumbnailRes: Int=R.drawable.offn,
    @DrawableRes val providerRes: Int=R.drawable.spotify_icon
)

//セッション一覧で取得する一件の方
data class SessionListResponseDto(
    val id: Int,
    val title: String = "",
    val imgUrl: String = "",
    val userCount: Int
)
