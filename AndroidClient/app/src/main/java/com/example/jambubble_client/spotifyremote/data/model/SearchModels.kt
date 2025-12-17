package com.example.jambubble_client.spotifyremote.data.model

//トラックのDto
data class Track(
    val id: String,
    val name: String,
    val uri: String,
    val artists: List<Artist>,
    val album: Album,
    val durationMs: Long,
    val explicit: Boolean,
    val popularity: Int,
    val previewUrl: String?
){
    val artistNames: String
        get() = artists.joinToString(", ") { it.name }

    val durationFormatted: String
        get(){
            val seconds = (durationMs / 1000) % 60
            val minutes = (durationMs / 1000) / 60
            return String.format("%d:%02d", minutes, seconds)
        }
}


//アーティスト情報
data class Artist(
    val id: String,
    val name: String,
    val uri: String,
    val images: List<Image>,
    val genres: List<String>,
    val popularity: Int,
    val followers: Followers?
) {
    val imageUrl: String?
        get() = images.firstOrNull()?.url

    val followersFormatted: String
        get() = followers?.total?.let { formatNumber(it) } ?: "0"

    private fun formatNumber(num: Int): String {
        return when {
            num >= 1_000_000 -> String.format("%.1fM", num / 1_000_000.0)
            num >= 1_000 -> String.format("%.1fK", num / 1_000.0)
            else -> num.toString()
        }
    }
}


//Album情報
data class Album(
    val id: String,
    val name: String,
    val uri: String,
    val artists: List<Artist>,
    val images: List<Image>,
    val releaseDate: String,
    val totalTracks: Int,
    val albumType: String
) {
    val imageUrl: String?
        get() = images.firstOrNull()?.url

    val artistNames: String
        get() = artists.joinToString(", ") { it.name }

    val year: String
        get() = releaseDate.split("-").firstOrNull() ?: releaseDate
}

//プレイリスト
data class Playlist(
    val id: String,
    val name: String,
    val uri: String,
    val description: String?,
    val images: List<Image>,
    val owner: Owner,
    val tracks: TracksInfo,
    val public: Boolean
) {
    val imageUrl: String?
        get() = images.firstOrNull()?.url

    val trackCount: String
        get() = "${tracks.total} tracks"
}


//画像
data class Image(
    val url: String,
    val height: Int?,
    val width: Int?
)

//ふぉろわー
data class Followers(
    val total: Int
)

//オーナー情報
data class Owner(
    val id: String,
    val displayName: String
)

//トラック情報(数のみ)
data class TracksInfo(
    val total: Int
)

//検索結果ラッパー
sealed class SearchResult {
    data class TrackResult(val track: Track) : SearchResult()
    data class ArtistResult(val artist: Artist) : SearchResult()
    data class AlbumResult(val album: Album) : SearchResult()
    data class PlaylistResult(val playlist: Playlist) : SearchResult()
}

//検索タイプ
enum class SearchType(val value: String) {
    TRACK("track"),
    ARTIST("artist"),
    ALBUM("album"),
    PLAYLIST("playlist"),
    ALL("track,artist,album,playlist")
}