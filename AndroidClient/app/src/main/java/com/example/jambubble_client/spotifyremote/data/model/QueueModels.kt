package com.example.jambubble_client.spotifyremote.data.model

/**
 * キュー内の楽曲情報
 */
data class QueueTrack(
    val id: String,
    val spotifyTrackId: String,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val imageUrl: String,
    val durationMs: Long,
    val requestedBy: String,
    val addedAt: Long = System.currentTimeMillis()
)

/**
 * 再生キューの状態
 */
data class QueueState(
    val tracks: List<QueueTrack> = emptyList(),
    val currentIndex: Int = -1,
    val isPlaying: Boolean = false
) {
    val currentTrack: QueueTrack?
        get() = tracks.getOrNull(currentIndex)

    val hasNext: Boolean
        get() = currentIndex < tracks.size - 1

    val hasPrevious: Boolean
        get() = currentIndex > 0

    val isEmpty: Boolean
        get() = tracks.isEmpty()
}