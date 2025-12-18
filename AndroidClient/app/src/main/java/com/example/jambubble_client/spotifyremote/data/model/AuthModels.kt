package com.example.jambubble_client.spotifyremote.data.model

/**
 * トークン情報（Domain Model）
 */
data class SpotifyTokens(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val refreshToken: String?,
    val scope: String,
    val expiresAt: Long = System.currentTimeMillis() + (expiresIn * 1000L)
) {
    fun isValid(): Boolean {
        val now = System.currentTimeMillis()
        val buffer = 5 * 60 * 1000L  // 5分前
        return now < (expiresAt - buffer)
    }
}

/**
 * ユーザー情報（Domain Model）
 */
data class SpotifyUser(
    val id: String,
    val displayName: String?,
    val email: String?,
    val imageUrl: String?,
    val product: String?
) {
    val isPremium: Boolean
        get() = product == "premium"
}
