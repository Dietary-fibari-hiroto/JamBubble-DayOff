package com.example.jambubble_client.data.api.service

import com.example.jambubble_client.data.dto.LoginRequestDto
import com.example.jambubble_client.data.dto.TokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<TokenResponseDto>


}