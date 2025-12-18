package com.example.jambubble_client.util

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

object PkceUtil {

    fun generateCodeVerifier(): String =
        ByteArray(64).also { SecureRandom().nextBytes(it) }
            .let { Base64.encodeToString(it, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING) }

    fun generateCodeChallenge(verifier: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(verifier.toByteArray(Charsets.US_ASCII))
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }
}
