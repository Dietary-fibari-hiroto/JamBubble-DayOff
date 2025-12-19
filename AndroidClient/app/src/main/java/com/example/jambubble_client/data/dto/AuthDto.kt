package com.example.jambubble_client.data.dto

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class TokenResponseDto(
    val token:String
)