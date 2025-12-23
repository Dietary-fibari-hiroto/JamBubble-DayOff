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
import com.google.gson.reflect.TypeToken
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignalRManager(private val serverUrl: String) {
    private var hubConnection: HubConnection? = null
    private val gson = Gson()

    // SignalRの接続状態
    private val _connectionState = MutableStateFlow(HubConnectionState.DISCONNECTED)
    val connectionState: StateFlow<HubConnectionState> = _connectionState

    // プレイリスト状態
    private val _playlist = MutableStateFlow<List<PlaylistItem>>(emptyList())
    val playlist: StateFlow<List<PlaylistItem>> = _playlist

    // ゲスト一覧
    private val _guests = MutableStateFlow<List<Guest>>(emptyList())
    val guests: StateFlow<List<Guest>> = _guests

    // セッション終了フラグ
    private val _sessionClosed = MutableStateFlow(false)
    val sessionClosed: StateFlow<Boolean> = _sessionClosed

    fun connect() {
        Log.d("SignalR", "接続を試行中: $serverUrl/musicsessionhub")

        // すでに接続済みの場合は何もしない
        if (hubConnection?.connectionState == HubConnectionState.CONNECTED) {
            Log.d("SignalR", "すでに接続されています")
            return
        }

        // HubConnection を生成
        hubConnection = HubConnectionBuilder.create("$serverUrl/musicsessionhub")
            .build()

        // イベントリスナー登録
        setupListeners()

        try {
            // SignalRへ接続開始
            hubConnection?.start()?.blockingAwait()
            _connectionState.value =
                hubConnection?.connectionState ?: HubConnectionState.DISCONNECTED

            Log.d(
                "SignalR",
                "ハブへの接続に成功しました。現在の状態: ${_connectionState.value}"
            )
        } catch (e: Exception) {
            Log.e("SignalR", "ハブへの接続に失敗しました", e)
            throw e
        }
    }

    private fun setupListeners() {
        Log.d("SignalR", "SignalRのイベントリスナーを設定中")

        // プレイリスト更新イベント
        hubConnection?.on("PlaylistUpdated", { data: Any ->
            try {
                Log.d("SignalR", "=== PlaylistUpdated イベント受信 ===")
                Log.d("SignalR", "受信データ型: ${data.javaClass.name}")
                Log.d("SignalR", "受信した生データ: $data")

                // Gsonで一度JSONに変換してからパース
                val jsonString = gson.toJson(data)
                Log.d("SignalR", "JSON文字列: $jsonString")

                val listType = object : TypeToken<List<PlaylistItem>>() {}.type
                val items: List<PlaylistItem> = gson.fromJson(jsonString, listType)

                Log.d("SignalR", "パース成功: ${items.size} 件の曲を取得")
                items.forEachIndexed { index, item ->
                    Log.d(
                        "SignalR",
                        "  [$index] ${item.trackName} - ${item.artistName}"
                    )
                }

                // StateFlowを更新
                _playlist.value = items
                Log.d("SignalR", "プレイリストのStateFlowを更新しました")
            } catch (e: Exception) {
                Log.e("SignalR", "PlaylistUpdated 処理中にエラーが発生しました", e)
                e.printStackTrace()
            }
        }, Any::class.java)

        // ゲスト参加イベント
        hubConnection?.on("GuestJoined", { data: Any ->
            try {
                Log.d("SignalR", "=== GuestJoined イベント受信 ===")
                Log.d("SignalR", "受信した生データ: $data")

                val jsonString = gson.toJson(data)
                val guestObj = gson.fromJson(jsonString, Guest::class.java)

                // ゲスト一覧に追加
                _guests.value = _guests.value + guestObj
                Log.d(
                    "SignalR",
                    "ゲスト参加: ${guestObj.name} / 現在のゲスト数: ${_guests.value.size}"
                )
            } catch (e: Exception) {
                Log.e("SignalR", "GuestJoined 処理中にエラーが発生しました", e)
                e.printStackTrace()
            }
        }, Any::class.java)

        // セッション終了イベント
        hubConnection?.on("SessionClosed", {
            Log.d("SignalR", "=== SessionClosed イベント受信 ===")
            _sessionClosed.value = true
        })

        Log.d("SignalR", "すべてのイベントリスナーの登録が完了しました")
    }

    // セッション作成
    suspend fun createSession(request: CreateSessionRequest): CreateSessionResponse? {
        return try {
            Log.d(
                "SignalR",
                "セッションを作成します。ホスト端末ID: ${request.hostDeviceId}"
            )

            val response = hubConnection?.invoke(
                CreateSessionResponse::class.java,
                "CreateSession",
                request
            )?.blockingGet()

            Log.d("SignalR", "セッション作成完了: ${response?.sessionId}")
            response
        } catch (e: Exception) {
            Log.e("SignalR", "セッション作成中にエラーが発生しました", e)
            e.printStackTrace()
            null
        }
    }

    // 曲追加
    suspend fun addTrack(request: AddTrackRequest): Boolean {
        return try {
            Log.d(
                "SignalR",
                "曲を追加します: ${request.trackName} / セッションID: ${request.sessionId}"
            )

            val success = hubConnection?.invoke(
                Boolean::class.java,
                "AddTrack",
                request
            )?.blockingGet() ?: false

            Log.d("SignalR", "曲追加結果: $success")
            success
        } catch (e: Exception) {
            Log.e("SignalR", "曲追加中にエラーが発生しました", e)
            e.printStackTrace()
            false
        }
    }

    // プレイリスト並び替え
    suspend fun reorderPlaylist(request: ReorderPlaylistRequest): Boolean {
        return try {
            Log.d(
                "SignalR",
                "プレイリストを並び替えます / セッションID: ${request.sessionId}"
            )

            val success = hubConnection?.invoke(
                Boolean::class.java,
                "ReorderPlaylist",
                request
            )?.blockingGet() ?: false

            Log.d("SignalR", "並び替え結果: $success")
            success
        } catch (e: Exception) {
            Log.e("SignalR", "プレイリスト並び替え中にエラーが発生しました", e)
            e.printStackTrace()
            false
        }
    }

    // 曲削除
    suspend fun removeTrack(request: RemoveTrackRequest): Boolean {
        return try {
            Log.d(
                "SignalR",
                "曲を削除します: ${request.itemId} / セッションID: ${request.sessionId}"
            )

            val success = hubConnection?.invoke(
                Boolean::class.java,
                "RemoveTrack",
                request
            )?.blockingGet() ?: false

            Log.d("SignalR", "曲削除結果: $success")
            success
        } catch (e: Exception) {
            Log.e("SignalR", "曲削除中にエラーが発生しました", e)
            e.printStackTrace()
            false
        }
    }

    // セッション終了
    suspend fun closeSession(sessionId: String): Boolean {
        return try {
            Log.d("SignalR", "セッションを終了します: $sessionId")

            val success = hubConnection?.invoke(
                Boolean::class.java,
                "CloseSession",
                sessionId
            )?.blockingGet() ?: false

            Log.d("SignalR", "セッション終了結果: $success")
            success
        } catch (e: Exception) {
            Log.e("SignalR", "セッション終了中にエラーが発生しました", e)
            e.printStackTrace()
            false
        }
    }

    // SignalR切断
    fun disconnect() {
        Log.d("SignalR", "ハブとの接続を切断します")
        hubConnection?.stop()
        _connectionState.value = HubConnectionState.DISCONNECTED
        Log.d("SignalR", "ハブとの接続を切断しました")
    }
}