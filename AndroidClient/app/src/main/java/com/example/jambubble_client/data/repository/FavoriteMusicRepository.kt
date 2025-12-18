package com.example.jambubble_client.data.repository

import android.content.Context
import android.util.Log
import com.example.jambubble_client.Config
import com.example.jambubble_client.data.api.service.FavoriteMusicApiService
import com.example.jambubble_client.data.dto.FavoriteMusicSummary
import com.example.jambubble_client.util.SecureStorage

class FavoriteMusicRepository(
    private val api: FavoriteMusicApiService,
    private val context: Context
){
    suspend fun getFavoriteMusicRanking(): Result<List<FavoriteMusicSummary>> {
        return try {
            val token = SecureStorage.load(context, Config.ACCESS_TOKEN) ?: throw IllegalStateException("Access token is null")

            val response = api.getFavoriteMusicRanking("Bearer $token")
            Log.d("TAG", "getFavoriteMusicRanking: $response")
            Result.success(response)
        }catch(e:Exception){
            Result.failure(e)
        }

    }
}

sealed class FavoriteMusicState{
    object Loading:SessionState()
    object Success:SessionState()
    object Failure:SessionState()
}