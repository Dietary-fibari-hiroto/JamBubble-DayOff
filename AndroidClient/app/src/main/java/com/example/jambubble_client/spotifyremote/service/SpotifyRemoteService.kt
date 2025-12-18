package com.example.jambubble_client.spotifyremote.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.jambubble_client.Config
import com.example.jambubble_client.spotifyremote.data.model.PlayerState
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/*
* SpotifyAppRemote接続を管理するバックグラウンドService
*
* ここではアプリとの接続を維持したり再生状態の管理とかコントロール操作までやる
* */
class SpotifyRemoteService : Service(){
    companion object{
        private const val TAG = "SpotifyRemoteService"
        private const val CLIENT_ID = Config.SPOTIFY_CLIENT_ID
        private const val REDIRECT_URL = Config.SPOTIFY_REDIRECT_URL
    }

    private val binder = LocalBinder()


    inner class LocalBinder: Binder(){
        fun getService():SpotifyRemoteService = this@SpotifyRemoteService
    }

    //SpotifyAppRemoteのインスタンス
    private var spotifyAppRemote: SpotifyAppRemote? = null

    //接続状態
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    //プレイヤー状態
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState:StateFlow<PlayerState> = _playerState.asStateFlow()

    //えらーの状態
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage:StateFlow<String?> = _errorMessage.asStateFlow()

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG,"サービスバウンド")
        return binder
    }

    override fun onCreate(){
        super.onCreate()
        Log.d(TAG,"サービス生成")
    }

    override fun onDestroy(){
        super.onDestroy()
        disconnect()
        Log.d(TAG,"サービス破棄")
    }


    //Spotifyアプリに接続
    fun connect(){
        if(spotifyAppRemote != null){
            Log.d(TAG,"すでにコネクトしてまっせ")
            return
        }

        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URL)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(
            this,
            connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(remote: SpotifyAppRemote){
                    spotifyAppRemote = remote
                    _isConnected.value = true
                    _errorMessage.value = null
                    Log.d(TAG,"コネクトしました！")

                    subscribeToPlayerState()
                }

                override fun onFailure(throwable: Throwable) {
                    _isConnected.value = false
                    _errorMessage.value = throwable.message ?: "Connection failed"
                    Log.e(TAG, "Connection failed", throwable)
                }
            }
        )
    }


    //Spotifyアプリから切断
    fun disconnect(){
        spotifyAppRemote?.let{
            SpotifyAppRemote.disconnect(it)
            spotifyAppRemote = null
        }
        _isConnected.value = false
        _playerState.value = PlayerState()
        Log.d(TAG,"コネクト解除しました")
    }

    //プレイヤー状態の監視を開始
    private fun subscribeToPlayerState() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback { state ->
            val track = state.track

            _playerState.value = PlayerState(
                trackName = track?.name ?: "",
                artistName = track?.artist?.name ?: "",
                albumName = track?.album?.name ?: "",
                imageUri = track?.imageUri?.raw ?: "",
                isPaused = state.isPaused,
                playbackPosition = state.playbackPosition,
                duration = track?.duration ?: 0L,
                isShuffling = state.playbackOptions.isShuffling,
                repeatMode = when (state.playbackOptions.repeatMode) {
                    0 -> PlayerState.RepeatMode.OFF
                    1 -> PlayerState.RepeatMode.TRACK
                    2 -> PlayerState.RepeatMode.CONTEXT
                    else -> PlayerState.RepeatMode.OFF
                }
            )
            Log.d(TAG, "プレイヤーの状態を更新しました。: ${_playerState.value.displayInfo}")

        }
    }

    //以下再生コントロール
    fun togglePlayPause(){
        spotifyAppRemote?.let{remote ->
            if(_playerState.value.isPaused){
                remote.playerApi.resume()
                Log.d(TAG,"再生")
            }else{
                remote.playerApi.pause()
                Log.d(TAG,"一時停止")
            }
        }?:run{
            _errorMessage.value = "Spotifyアプリに接続されていません"
        }
    }

    //再生
    fun play(){
        spotifyAppRemote?.playerApi?.resume()
        Log.d(TAG,"再生")
    }

    //一時停止
    fun pause(){
        spotifyAppRemote?.playerApi?.pause()
        Log.d(TAG,"一時停止")
    }

    //次の曲へ
    fun skipNext(){
        spotifyAppRemote?.playerApi?.skipNext()
        Log.d(TAG,"次の曲へ")
    }

    //前の曲へ
    fun skipPrevious() {
        spotifyAppRemote?.playerApi?.skipPrevious()
        Log.d(TAG, "前の曲へ")
    }

    //特定のトラックを再生
    fun playTrack(uri:String){
        spotifyAppRemote?.playerApi?.play(uri)
        Log.d(TAG,"特定のトラックを再生")
    }

    //シャッフルモード切替
    fun toggleShuffle() {
        spotifyAppRemote?.playerApi?.setShuffle(!_playerState.value.isShuffling)
        Log.d(TAG, "シャッフルモード切り替え:${!_playerState.value.isShuffling}")
    }

    //リピートモード切替
    fun toggleRepeat(){
        val nextMode = when(_playerState.value.repeatMode){
            PlayerState.RepeatMode.OFF -> 2 //CONTEXT
            PlayerState.RepeatMode.CONTEXT -> 1 //TRACK
            PlayerState.RepeatMode.TRACK -> 0 //OFF

        }

    }


    /**
     * シーク位置へ移動
     * @param positionMs ミリ秒単位の位置
     */
    fun seekTo(positionMs: Long){
        spotifyAppRemote?.playerApi?.seekTo(positionMs)
        Log.d(TAG,"シーク位置へ移動:$positionMs ms")
    }



}

