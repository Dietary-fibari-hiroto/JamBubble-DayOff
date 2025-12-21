package com.example.jambubble_client.data.api.service

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface FriendApiService{
    @POST("/api/friend/{targetUserId}/request")
    suspend fun friendRequest(
        @Path("targetUserId") id: Int,
        @Header("Authorization")token:String
    ): Response<Unit>
}
