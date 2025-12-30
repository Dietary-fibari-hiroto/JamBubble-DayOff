package com.example.jambubble_client.data.model

import com.google.gson.annotations.SerializedName

data class Session(
    val sessionId: String,
    val hostConnectionId: String,
    val createdAt: String,
    val lastActivity: String,
    val playlist: List<PlaylistItem>,
    val guests: List<Guest>,
    val status: SessionStatus
)

data class PlaylistItem(
    val id: String,
    val spotifyTrackId: String,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val albumImageUrl: String,
    val durationMs: Int,
    val requestedBy: String,
    val requestedByUserId: String,
    val requestedAt: String,
    val order: Int,

    @SerializedName("status")
    val status: PlaybackStatus? = null
)

enum class PlaybackStatus {
    PENDING,    // 未再生
    PLAYING,    // 再生中
    COMPLETED   // 再生済み
}
data class Guest(
    val userId: String,
    val name: String,
    val connectionId: String,
    val joinedAt: String
)

enum class SessionStatus {
    Active,
    Inactive,
    Closed
}

data class CreateSessionRequest(
    val hostDeviceId: String
)

data class CreateSessionResponse(
    val sessionId: String,
    val guestUrl: String
)

data class AddTrackRequest(
    val sessionId: String,
    val spotifyTrackId: String,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val albumImageUrl: String,
    val durationMs: Int,
    val requestedBy: String,
    val requestedByUserId: String
)

data class ReorderPlaylistRequest(
    val sessionId: String,
    val orderedItemIds: List<String>
)

data class RemoveTrackRequest(
    val sessionId: String,
    val itemId: String,
    val requestedByUserId: String
)

data class SpotifyTrackSearchResult(
    val id: String,
    val name: String,
    val artist: String,
    val album: String,
    val albumImageUrl: String,
    val durationMs: Int
)