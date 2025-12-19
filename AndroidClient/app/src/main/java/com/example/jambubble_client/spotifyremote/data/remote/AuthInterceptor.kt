package com.example.jambubble_client.spotifyremote.data.remote

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 認証インターセプター
 *
 * すべてのAPIリクエストにアクセストークンを自動付与
 */
class AuthInterceptor(
    private val tokenProvider: suspend () -> String?
) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // トークン取得エンドポイントは認証不要
        if (originalRequest.url.encodedPath.contains("/api/token")) {
            return chain.proceed(originalRequest)
        }

        // アクセストークン取得
        val token = runBlocking {
            tokenProvider()
        }

        if (token == null) {
            Log.w(TAG, "アクセストークンがありません")
            return chain.proceed(originalRequest)
        }

        // Authorizationヘッダー追加
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
