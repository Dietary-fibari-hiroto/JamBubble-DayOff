package com.example.jambubble_client.data.network

import android.util.Log
import com.example.jambubble_client.data.model.AddTrackRequest
import com.example.jambubble_client.data.model.CreateSessionRequest
import com.example.jambubble_client.data.model.CreateSessionResponse
import com.example.jambubble_client.data.model.Guest
import com.example.jambubble_client.data.model.PlaylistItem
import com.example.jambubble_client.data.model.RemoveTrackRequest
import com.example.jambubble_client.data.model.ReorderPlaylistRequest
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignalRManager(private val serverUrl:String){
    private var hubConnection: HubConnection? = null
    private val gson = Gson()

    private val _connectionState = MutableStateFlow(HubConnectionState.DISCONNECTED)
    val connectionState: StateFlow<HubConnectionState> = _connectionState

    private val _playlist = MutableStateFlow<List<PlaylistItem>>(emptyList())
    val playlist:StateFlow<List<PlaylistItem>> = _playlist

    private val _guests = MutableStateFlow<List<Guest>>(emptyList())
    val guests:StateFlow<List<Guest>> = _guests

    private val _sessionClosed = MutableStateFlow(false)
    val sessionClosed:StateFlow<Boolean> = _sessionClosed

    fun connect(){
        if(hubConnection?.connectionState == HubConnectionState.CONNECTED){
            return
        }

        hubConnection = HubConnectionBuilder.create("$serverUrl/musicsessionhub").build()

        setupListeners()

        hubConnection?.start()?.blockingAwait()
        _connectionState.value = hubConnection?.connectionState ?: HubConnectionState.DISCONNECTED
        Log.d("SignalR","ハブへコネクトしました。")
    }

    private fun setupListeners() {
        hubConnection?.on("PlaylistUpdated", { playlist: String ->
            try {
                val items = gson.fromJson(playlist, Array<PlaylistItem>::class.java).toList()
                _playlist.value = items
                Log.d("SignalR", "プレイリスト更新：${items.size}件")
            } catch (e: Exception) {
                Log.e("SignalR", "プレイリスト更新エラー：${e.message}")
            }
        }, String::class.java)

        hubConnection?.on("GuestJoined", { guest: String ->
            try {
                val guestObj = gson.fromJson(guest, Guest::class.java)
                _guests.value = _guests.value + guestObj
                Log.d("SignalR", "ゲスト参加：${guestObj.name}")
            } catch (e: Exception) {
                Log.e("SignalR", "ゲスト参加エラー：${e.message}")
            }
        }, String::class.java)

        hubConnection?.on("SessionClosed", {
            _sessionClosed.value = true
            Log.d("SignalR", "セッション終了")
        })

    }


    suspend fun createSession(request: CreateSessionRequest): CreateSessionResponse?{
        return try{
            hubConnection?.invoke(
                CreateSessionResponse::class.java,
                "CreateSession",
                request
            )?.blockingGet()
        }catch (e:Exception){
            Log.e("SignalR","セッション作成エラー：${e.message}")
            null
        }
    }

    suspend fun addTrack(request: AddTrackRequest):Boolean{
        return try{
            hubConnection?.invoke(
                Boolean::class.java,
                "AddTrack",
                request
            )?.blockingGet()?:false
        }catch (e:Exception){
            Log.e("SignalR","トラック追加エラー：${e.message}")
            false
        }
    }

    suspend fun reorderPlaylist(request: ReorderPlaylistRequest):Boolean {
        return try {
            hubConnection?.invoke(
                Boolean::class.java,
                "ReorderPlaylist",
                request
            )?.blockingGet() ?: false
        } catch (e: Exception) {
            Log.e("SignalR", "プレイリスト再配置エラー：${e.message}")
            false
        }
    }

    suspend fun removeTrack(request: RemoveTrackRequest):Boolean{
        return try{
            hubConnection?.invoke(
                Boolean::class.java,
                "RemoveTrack",
                request
            )?.blockingGet()?:false
        }catch (e:Exception){
            Log.e("SignalR","トラック削除エラー：${e.message}")
            false
        }
    }

    suspend fun closeSession(sessionId:String):Boolean{
        return try{
            hubConnection?.invoke(
                Boolean::class.java,
                "CloseSession",
                sessionId
            )?.blockingGet()?:false
        }catch (e: Exception){
            Log.e("SignalR","セッション終了エラー：${e.message}")
            false
        }
    }

    fun disconnect() {
        hubConnection?.stop()
        _connectionState.value = HubConnectionState.DISCONNECTED
        Log.d("SignalR", "ハブから切断しました。")
    }

}