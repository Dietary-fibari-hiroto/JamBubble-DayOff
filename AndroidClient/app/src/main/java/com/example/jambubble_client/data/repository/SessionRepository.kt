package com.example.jambubble_client.data.repository

import android.content.Context
import android.util.Log
import com.example.jambubble_client.Config
import com.example.jambubble_client.data.api.service.SessionApiService
import com.example.jambubble_client.data.dto.SessionListResponseDto
import com.example.jambubble_client.util.SecureStorage

class SessionRepository(
    private val api: SessionApiService,
    private val context: Context
) {

    suspend fun getFavoritePlaylist(): Result<List<SessionListResponseDto>> {
        return try{
            val response = api.getFavoritePlaylist()
            Log.d("TAG", "getFavoritePlaylist: $response")
            Result.success(response)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getFriendPlaylist(): Result<List<SessionListResponseDto>> {
        return try {
            val token = SecureStorage.load(context, Config.ACCESS_TOKEN)
                ?: throw IllegalStateException("Access token is null")


            val response = api.getFriendPlaylist( "Bearer $token")
            Log.d("TAG", "getFriendPlaylist: $response")
            Result.success(response)
        }catch (e:Exception){
            Log.d("TAG", "getFriendPlaylist: $e")
            Result.failure(e)
        }
    }
}

sealed class SessionState{
    object Loading:SessionState()
    object Success:SessionState()
    object Failure:SessionState()
}