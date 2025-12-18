package com.example.jambubble_client.data.api.service

import com.example.jambubble_client.data.dto.FavoriteMusicSummary
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FavoriteMusicApiService{
    @GET("/api/favorite-song/ranking")
    suspend fun getFavoriteMusicRanking(
        @Header("Authorization") token:String,
        @Query("n")n:Int=10,
        @Query("skip")skip:Int=0
    ):List<FavoriteMusicSummary>
}