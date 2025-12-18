package com.example.jambubble_client.data.api.service

import com.example.jambubble_client.data.dto.OtherUserProfileDto
import com.example.jambubble_client.data.dto.UserProfileDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApiService {
    @Multipart
    @POST("/api/user")
    suspend fun postUser(
        @Part("Name") name: RequestBody,
        @Part("Email") email: RequestBody,
        @Part("Password") password: RequestBody,
        @Part("Gender") gender: RequestBody,
        @Part("Birthday") birthday: RequestBody,
        @Part userImage: MultipartBody.Part?  // nullable
    ): Response<Unit>  // または適切なレスポンス型

    @GET("/api/user")
    suspend fun getUser(
        @Header("Authorization") token:String
    ): UserProfileDto

    //特定のユーザーの情報を返す
    @GET("/api/user/{id}")
    suspend fun getUserById(
        @Path("id") id: Int,
        @Header("Authorization") token:String
    ): OtherUserProfileDto
}