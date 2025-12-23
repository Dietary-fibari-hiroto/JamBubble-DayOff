package com.example.jambubble_client.data.repository

import android.content.Context
import android.util.Log
import com.example.jambubble_client.Config
import com.example.jambubble_client.data.api.service.FriendApiService
import com.example.jambubble_client.util.SecureStorage

class FriendRepository(
    private val api: FriendApiService,
    private val context: Context
) {
    suspend fun postFriendRequest(id: Int): FriendRequestResult {

        return try {
            val token = SecureStorage.load(context, Config.ACCESS_TOKEN)
                ?: throw IllegalStateException("Access token is null")

            val response = api.friendRequest(id, "Bearer $token")

            when (response.code()) {
                200 -> FriendRequestResult.Success
                409 -> FriendRequestResult.AlreadyFriend
                410,400 -> FriendRequestResult.AlreadyRequested
                404 -> FriendRequestResult.NotFound
                else -> FriendRequestResult.Error(Exception("Unknown error"))
            }
        }catch (e:Exception){
            Log.d("TAG", "postFriendRequest: $e")
            FriendRequestResult.Error(e)
        }


        /**
         * ViewModelのコード例(メモ)
         * fun requestFriend(targetUserId: Int) {
         *     viewModelScope.launch {
         *         friendRepository.requestFriend(targetUserId)
         *             .onSuccess {
         *                 _uiState.value = FriendRequestEnum.Requested
         *             }
         *             .onFailure {
         *                 _uiState.value = FriendRequestEnum.Default
         *             }
         *     }
         * }
         *
         */
    }
}

//フレンド申請の状態管理
sealed class FriendRequestResult {
    object Success : FriendRequestResult()
    object AlreadyFriend : FriendRequestResult()
    object AlreadyRequested : FriendRequestResult()
    object NotFound : FriendRequestResult()
    data class Error(val throwable: Throwable) : FriendRequestResult()
}
