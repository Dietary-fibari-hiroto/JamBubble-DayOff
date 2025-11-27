package com.jambubble.spotify

import android.content.Context
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.spotify.protocol.types.PlayerState

class SpotifyWrapper(private val context: Context) {
    
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private val clientId = "" 
    private val redirectUri = ""
    
    interface ConnectionCallback {
        fun onConnected()
        fun onFailure(error: String)
    }
    
    interface PlayerStateCallback {
        fun onPlayerState(
            trackName: String,
            artistName: String,
            isPaused: Boolean,
            positionMs: Long,
            durationMs: Long
        )
        fun onError(error: String)
    }
    
    private var connectionCallback: ConnectionCallback? = null
    private var playerStateCallback: PlayerStateCallback? = null
    
    // �ڑ�
    fun connect(callback: ConnectionCallback) {
        this.connectionCallback = callback
        
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .build()
        
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(remote: SpotifyAppRemote) {
                spotifyAppRemote = remote
                callback.onConnected()
                setupPlayerStateSubscription()
            }
            
            override fun onFailure(error: Throwable) {
                callback.onFailure(error.message ?: "Unknown error")
            }
        })
    }
    
    // �ؒf
    fun disconnect() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
            spotifyAppRemote = null
        }
    }
    
    // �Đ�
    fun play(uri: String) {
        spotifyAppRemote?.playerApi?.play(uri)
    }
    
    // �ꎞ��~
    fun pause() {
        spotifyAppRemote?.playerApi?.pause()
    }
    
    // �ĊJ
    fun resume() {
        spotifyAppRemote?.playerApi?.resume()
    }
    
    // ���̋�
    fun skipNext() {
        spotifyAppRemote?.playerApi?.skipNext()
    }
    
    // �O�̋�
    fun skipPrevious() {
        spotifyAppRemote?.playerApi?.skipPrevious()
    }
    
    // �V�[�N
    fun seekTo(positionMs: Long) {
        spotifyAppRemote?.playerApi?.seekTo(positionMs)
    }
    
    // �v���C���[�X�e�[�g�w��
    private fun setupPlayerStateSubscription() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback { state ->
            playerStateCallback?.let { callback ->
                val track = state.track
                callback.onPlayerState(
                    trackName = track.name,
                    artistName = track.artist.name,
                    isPaused = state.isPaused,
                    positionMs = state.playbackPosition,
                    durationMs = track.duration
                )
            }
        }
    }
    
    // �v���C���[�X�e�[�g�R�[���o�b�N�ݒ�
    fun setPlayerStateCallback(callback: PlayerStateCallback) {
        this.playerStateCallback = callback
    }
    
    // ���݂̃v���C���[�X�e�[�g���擾
    fun getCurrentPlayerState() {
        spotifyAppRemote?.playerApi?.playerState?.setResultCallback { state ->
            playerStateCallback?.let { callback ->
                val track = state.track
                callback.onPlayerState(
                    trackName = track.name,
                    artistName = track.artist.name,
                    isPaused = state.isPaused,
                    positionMs = state.playbackPosition,
                    durationMs = track.duration
                )
            }
        }?.setErrorCallback { error ->
            playerStateCallback?.onError(error.message ?: "Unknown error")
        }
    }
    
    // �ڑ���Ԃ̊m�F
    fun isConnected(): Boolean {
        return spotifyAppRemote != null
    }
}