package com.example.jambubble_client.spotifyremote.data.repository

import android.util.Log
import com.example.jambubble_client.Config
import com.example.jambubble_client.spotifyremote.data.model.Track
import com.example.jambubble_client.spotifyremote.service.SpotifyApiService
import com.example.jambubble_client.util.Resource

/**
 * Spotify検索Repository
 */
class SpotifySearchRepository(
    private val apiService: SpotifyApiService
) {

    companion object {
        private const val TAG = "SpotifySearchRepository"
    }

    /**
     * トラック検索
     */
    suspend fun searchTracks(query: String): Resource<List<Track>> {
        return try {
            if (query.isBlank()) {
                return Resource.Error("検索クエリが空です")
            }

            Log.d(TAG, "検索: $query")

            val response = apiService.searchTracks(
                query = query,
                market = Config.DEFAULT_MARKET
            )

            if (response.isSuccessful && response.body() != null) {
                val tracks = response.body()!!.tracks?.items?.map { trackResponse ->
                    // Response → Domain Model変換
                    Track(
                        id = trackResponse.id,
                        name = trackResponse.name,
                        uri = trackResponse.uri,
                        artistName = trackResponse.artists.joinToString(", ") { it.name },
                        albumName = trackResponse.album.name,
                        imageUrl = trackResponse.album.images.firstOrNull()?.url,
                        durationMs = trackResponse.durationMs,
                        explicit = trackResponse.explicit
                    )
                } ?: emptyList()

                Log.d(TAG, "検索成功: ${tracks.size}件")
                Resource.Success(tracks)
            } else {
                Log.e(TAG, "検索失敗: ${response.code()}")
                Resource.Error("検索に失敗しました")
            }
        } catch (e: Exception) {
            Log.e(TAG, "検索エラー", e)
            Resource.Error("エラーが発生しました: ${e.message}")
        }
    }
}