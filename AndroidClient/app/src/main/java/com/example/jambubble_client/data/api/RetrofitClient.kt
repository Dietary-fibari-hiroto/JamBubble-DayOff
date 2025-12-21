package com.example.jambubble_client.data.api

import com.example.jambubble_client.data.api.service.AuthApiService
import com.example.jambubble_client.data.api.service.FavoriteMusicApiService
import com.example.jambubble_client.data.api.service.FriendApiService
import com.example.jambubble_client.data.api.service.SessionApiService
import com.example.jambubble_client.data.api.service.UserApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient{
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

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

    val friendApi: FriendApiService by lazy{
        retrofit.create(FriendApiService::class.java)
    }

}
