package com.example.jambubble_client.data.dto

data class FriendResponseDto(
    val id: Int,
    val name: String,
    val imgUrl: String?,
)

data class FriendRequestDto(
    val id: Int,
    val name: String,
    val imgUrl: String?,
    val state: Int
)

