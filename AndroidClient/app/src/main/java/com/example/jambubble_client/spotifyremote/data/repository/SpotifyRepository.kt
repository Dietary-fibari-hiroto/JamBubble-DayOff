package com.example.jambubble_client.spotifyremote.data.repository

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.jambubble_client.spotifyremote.data.model.PlayerState
import com.example.jambubble_client.spotifyremote.service.SpotifyRemoteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/*
* Spotify操作の窓口みたいなもん
* SpotifyRemoteServiceへのアクセス
* 複数の画面でプレイヤー状態を共有
* まぁUIとServiceの橋渡しやね
* */

class SpotifyRepository(private val application: Application) {

    companion object {
        private const val TAG = "SpotifyRepository"
    }

    //Repository専用のCoroutineScope
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    //Serviceリファレンス
    private var service: SpotifyRemoteService? = null

    // Serviceバインディング状態
    private val _isServiceBound = MutableStateFlow(false)
    val isServiceBound: StateFlow<Boolean> = _isServiceBound.asStateFlow()

    //Spotify接続状態(Serviceから転送)
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    //プレイヤー状態
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    //エラーメッセージ
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    //Serviceが接続したときのコールバック
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Log.d(TAG, "サービスに接続しました。")
            val localBinder = binder as SpotifyRemoteService.LocalBinder
            service = localBinder.getService()
            _isServiceBound.value = true

            //Serviceの状態をRepositoryに転送開始
            observeServiceStates()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "サービスから切断されました。")
            service = null
            _isServiceBound.value = false
            _isConnected.value = false
        }
    }


    //Serviceの状態を監視してRepositoryに反映
    private fun observeServiceStates() {
        service?.let { svc ->
            //接続状態の監視
            repositoryScope.launch {
                svc.isConnected.collect { connected ->
                    Log.d(TAG, "接続状態が変更されました: $connected")
                    _isConnected.value = connected
                }
            }

            //プレイヤー状態の監視
            repositoryScope.launch {
                svc.playerState.collect { state ->
                    Log.d(TAG, "プレイヤー状態が変更されました: ${state.displayInfo}")
                    _playerState.value = state
                }
            }

            //エラーメッセージの監視
            repositoryScope.launch {
                svc.errorMessage.collect { error ->
                    _errorMessage.value = error
                    if (error != null) {
                        Log.e(TAG, "エラー: $error")
                    }
                }
            }
        }
    }

//Serviceにバインド
    fun bindService() {
        if (_isServiceBound.value) {
            Log.d(TAG, "サービスは既にバインド済みです。")
            return
        }

        Log.d(TAG, "サービスにバインドします。")
        val intent = Intent(application, SpotifyRemoteService::class.java)
        val bound = application.bindService(
            intent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        if (bound) {
            Log.d(TAG, "バインドリクエスト成功")
        } else {
            Log.e(TAG, "バインド失敗")
        }
    }


    //Serviceからアンバインド
    fun unbindService() {
        if (_isServiceBound.value) {
            application.unbindService(serviceConnection)
            _isServiceBound.value = false
            service = null
            Log.d(TAG, "サービスからアンバインドしました。")
        }
    }


    //Repositoryのクリーンアップ
    fun cleanup() {
        repositoryScope.cancel()
        unbindService()
    }


    //Spotifyに接続
    fun connect() {
        if (!_isServiceBound.value) {
            Log.e(TAG, "接続できません: サービスがバインドされていません")
            _errorMessage.value = "Service not ready"
            return
        }
        Log.d(TAG, "Spotify接続を開始します。")
        service?.connect()
    }

    //Spotifyから切断
    fun disconnect() {
        Log.d(TAG, "Spotifyから切断します。")
        service?.disconnect()
    }

    //再生コントロール操作
    fun togglePlayPause() {
        checkServiceAndExecute("togglePlayPause") { it.togglePlayPause() }
    }

    fun play() {
        checkServiceAndExecute("play") { it.play() }
    }

    fun pause() {
        checkServiceAndExecute("pause") { it.pause() }
    }

    fun skipNext() {
        checkServiceAndExecute("skipNext") { it.skipNext() }
    }

    fun skipPrevious() {
        checkServiceAndExecute("skipPrevious") { it.skipPrevious() }
    }

    fun playTrack(uri: String) {
        checkServiceAndExecute("playTrack") { it.playTrack(uri) }
    }

    fun toggleShuffle() {
        checkServiceAndExecute("toggleShuffle") { it.toggleShuffle() }
    }

    fun toggleRepeat() {
        checkServiceAndExecute("toggleRepeat") { it.toggleRepeat() }
    }

    fun seekTo(positionMs: Long) {
        checkServiceAndExecute("seekTo") { it.seekTo(positionMs) }
    }


    //Serviceが利用可能かチェックして実行
    private fun checkServiceAndExecute(
        operation: String,
        action: (SpotifyRemoteService) -> Unit
    ) {
        service?.let { svc ->
            Log.d(TAG, "操作実行: $operation")
            action(svc)
        } ?: run {
            Log.e(TAG, "$operation を実行できません: サービスがバインドされていません")
            _errorMessage.value = "Service not available"
        }
    }
}






