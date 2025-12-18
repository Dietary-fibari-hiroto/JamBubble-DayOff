package com.example.jambubble_client.spotifyremote.data.remote

import com.google.gson.annotations.SerializedName

/**
 * 認証トークンレスポンス
 */
data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_in")
    val expiresIn: Int,

    @SerializedName("refresh_token")
    val refreshToken: String?,

    @SerializedName("scope")
    val scope: String
)

/**
 * ユーザー情報レスポンス
 */
data class UserResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("display_name")
    val displayName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("images")
    val images: List<ImageResponse>?,

    @SerializedName("product")
    val product: String?
)

/**
 * 検索レスポンス
 */
data class SearchResponse(
    @SerializedName("tracks")
    val tracks: TracksPage?
)

data class TracksPage(
    @SerializedName("items")
    val items: List<TrackResponse>
)

data class TrackResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("uri")
    val uri: String,

    @SerializedName("artists")
    val artists: List<ArtistResponse>,

    @SerializedName("album")
    val album: AlbumResponse,

    @SerializedName("duration_ms")
    val durationMs: Long,

    @SerializedName("explicit")
    val explicit: Boolean
)

data class ArtistResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)

data class AlbumResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("images")
    val images: List<ImageResponse>
)

data class ImageResponse(
    @SerializedName("url")
    val url: String
)
