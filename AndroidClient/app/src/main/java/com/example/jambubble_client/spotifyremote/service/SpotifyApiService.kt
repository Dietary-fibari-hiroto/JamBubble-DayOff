package com.example.jambubble_client.spotifyremote.service

import com.example.jambubble_client.spotifyremote.data.remote.SearchResponse
import com.example.jambubble_client.spotifyremote.data.remote.TokenResponse
import com.example.jambubble_client.spotifyremote.data.remote.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Spotify API インターフェース
 */
interface SpotifyApiService {

    // ========== 認証 ==========

    /**
     * Authorization Code → Access Token交換
     */
    @POST("api/token")
    @FormUrlEncoded
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("client_id") clientId: String,
        @Field("code_verifier") codeVerifier: String
    ): Response<TokenResponse>

    /**
     * トークンリフレッシュ
     */
    @POST("api/token")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String
    ): Response<TokenResponse>

    // ========== ユーザー ==========

    /**
     * 現在のユーザー情報取得
     */
    @GET("v1/me")
    suspend fun getCurrentUser(): Response<UserResponse>

    // ========== 検索 ==========

    /**
     * トラック検索
     */
    @GET("v1/search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 50,
        @Query("market") market: String? = null
    ): Response<SearchResponse>
}