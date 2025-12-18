package com.example.jambubble_client.data.api

import com.example.jambubble_client.data.api.service.AuthApiService
import com.example.jambubble_client.data.api.service.FavoriteMusicApiService
import com.example.jambubble_client.data.api.service.SessionApiService
import com.example.jambubble_client.data.api.service.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient{
    private val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserApiService by lazy{
        retrofit.create(UserApiService::class.java)
    }

    val authApi: AuthApiService by lazy{
        retrofit.create(AuthApiService::class.java)
    }

    val sessionApi: SessionApiService by lazy {
        retrofit.create(SessionApiService::class.java)
    }

    val favoriteMusicApi: FavoriteMusicApiService by lazy {
        retrofit.create(FavoriteMusicApiService::class.java)
    }

}
