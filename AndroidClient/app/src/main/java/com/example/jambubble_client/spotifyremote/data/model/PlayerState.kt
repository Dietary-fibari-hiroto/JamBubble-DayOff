package com.example.jambubble_client.spotifyremote.data.model


//Spotify再生状態を表すデータクラス
data class PlayerState(
    val trackName: String = "",
    val artistName: String = "",
    val albumName: String = "",
    val imageUri: String = "",
    val isPaused: Boolean = true,
    val playbackPosition: Long = 0L,
    val duration: Long = 0L,
    val isShuffling: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF
) {
    enum class RepeatMode {
        OFF, TRACK, CONTEXT
    }

    val isPlaying: Boolean
        get() = !isPaused

    val hasTrack: Boolean
        get() = trackName.isNotEmpty()

    val displayInfo: String
        get() = if (hasTrack) "$trackName — $artistName" else "No track playing"
}