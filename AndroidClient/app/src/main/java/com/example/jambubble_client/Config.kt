package com.example.jambubble_client

object Config {
    const val ACCESS_TOKEN = BuildConfig.ACCESS_TOKEN
    const val SPOTIFY_CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID
    const val SPOTIFY_REDIRECT_URL = "jambubble://callback"


    const val SPOTIFY_ACCESS_TOKEN = BuildConfig.SPOTIFY_ACCESS_TOKEN
    const val SPOTIFY_REFRESH_TOKEN = BuildConfig.SPOTIFY_REFRESH_TOKEN

    const val SPOTIFY_API_BASE_URL = "https://api.spotify.com/"
    const val SPOTIFY_ACCOUNTS_BASE_URL = "https://accounts.spotify.com/"
    const val SPOTIFY_AUTH_URL = "https://accounts.spotify.com/authorize"

    const val KEY_CODE_VERIFIER = "code_verifier"
    const val KEY_TOKEN_EXPIRY = "spotify_token_expiry"

    // ========== スコープ（要求する権限） ==========
    const val SPOTIFY_SCOPES =        "user-read-private " +
            "user-read-email " +
            "playlist-read-private " +
            "user-library-read " +
            "user-top-read"

    const val DEFAULT_MARKET = "JP"
    const val NETWORK_TIMEOUT = 30L
}

