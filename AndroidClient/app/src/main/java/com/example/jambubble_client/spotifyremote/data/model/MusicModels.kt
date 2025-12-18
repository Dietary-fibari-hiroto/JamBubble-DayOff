package com.example.jambubble_client.spotifyremote.data.model

/**
 * トラック（Domain Model）
 */
data class Track(
    val id: String,
    val name: String,
    val uri: String,
    val artistName: String,
    val albumName: String,
    val imageUrl: String?,
    val durationMs: Long,
    val explicit: Boolean
) {
    val durationFormatted: String
        get() {
            val seconds = (durationMs / 1000) % 60
            val minutes = (durationMs / 1000) / 60
            return String.format("%d:%02d", minutes, seconds)
        }
}

/**
 * アーティスト（Domain Model）
 */
data class Artist(
    val id: String,
    val name: String,
    val imageUrl: String?
)

/**
 * アルバム（Domain Model）
 */
data class Album(
    val id: String,
    val name: String,
    val artistName: String,
    val imageUrl: String?,
    val releaseYear: String
)

/**
 * プレイリスト（Domain Model）
 */
data class Playlist(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val trackCount: Int
)

/**
 * 検索結果
 */
sealed class SearchResult {
    data class TrackResult(val track: Track) : SearchResult()
    data class ArtistResult(val artist: Artist) : SearchResult()
    data class AlbumResult(val album: Album) : SearchResult()
}