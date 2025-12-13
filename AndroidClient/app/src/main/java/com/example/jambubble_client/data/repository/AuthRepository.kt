package com.example.jambubble_client.data.repository

import android.content.Context
import android.util.Log
import com.example.jambubble_client.Config
import com.example.jambubble_client.data.api.service.AuthApiService
import com.example.jambubble_client.data.dto.LoginRequestDto
import com.example.jambubble_client.data.dto.UserProfileDto
import com.example.jambubble_client.util.SecureStorage

class AuthRepository(
    private val api: AuthApiService,
    private val context: Context
) {





    suspend fun login(email: String, password: String): Result<Unit> {
        return try{
            val response = api.login(
                LoginRequestDto(email,password)
            )
            Log.d("TAG", "デバッグ用")
            if(response.isSuccessful){
                val token = response.body()?.token?:return Result.failure(Exception("トークンがないよーて"))

                SecureStorage.save(context, Config.ACCESS_TOKEN, token)
                Result.success(Unit)
            }else{
                Result.failure(Exception("ログインに失敗しました: ${response.code()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}


sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val user: UserProfileDto) : AuthState()
}
