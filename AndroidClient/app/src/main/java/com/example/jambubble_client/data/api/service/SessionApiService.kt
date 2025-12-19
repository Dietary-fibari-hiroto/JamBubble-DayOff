package com.example.jambubble_client.data.api.service

import com.example.jambubble_client.data.dto.SessionListResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SessionApiService {
    @GET("/api/session/popular")
    suspend fun getFavoritePlaylist(
        @Query("n") n:Int = 10,
        @Query("skip") skip: Int = 0
    ):List<SessionListResponseDto>

    @GET("/api/session/friend")
    suspend fun getFriendPlaylist(
        @Header("Authorization") token:String,
        @Query("n") n:Int = 10,
        @Query("skip") skip: Int = 0
    ):List<SessionListResponseDto>

}