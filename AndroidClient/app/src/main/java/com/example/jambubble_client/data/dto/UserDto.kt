package com.example.jambubble_client.data.dto

import android.net.Uri
import kotlinx.serialization.Serializable

data class GenderDto(
    val id:Int = 0,//0.その他,1.男,2:女
)

//ユーザー登録時
data class UserRegisterDto(
    val name:String,
    val email:String,
    val password:String,
    val gender:Int,
    val birthday:String,
    val userImage: Uri?
)

@Serializable
data class UserProfileDto(
    val id: Int,
    val name: String,
    val birthday: String,
    val email: String,
    val gender: Int,
    val isStreetPass:Boolean,
    val imgUrl: String ="https://dawn-waiting.com/static/media/dawn_cat_ani.863d6550f404cf074627.png",
    val message:String?,
    val sessionCount:Int,
    val musicId:String?
)

@Serializable
data class OtherUserProfileDto(
    val name: String,
    val gender: Int,
    val imgUrl: String?,
    val message:String?,
    val sessionCount:Int,
    val musicId:String?
)