package com.example.jambubble_client.ui.viewmodel.friends

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.data.api.RetrofitClient
import com.example.jambubble_client.data.dto.OtherUserProfileDto
import com.example.jambubble_client.data.repository.FriendRepository
import com.example.jambubble_client.data.repository.FriendRequestResult
import com.example.jambubble_client.data.repository.UserRepository
import com.example.jambubble_client.ui.screens.friends.RequestResultEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendAddViewModel(
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository
): ViewModel() {

    private val _requestState = MutableStateFlow(RequestResultEnum.Default)
    val requestState: StateFlow<RequestResultEnum> = _requestState


    private val _friendState =
        MutableStateFlow<Result<OtherUserProfileDto>?>(null)
    val friendState: StateFlow<Result<OtherUserProfileDto>?> = _friendState

    fun loadUserById(id: Int) {
        viewModelScope.launch{
            val result = userRepository.userById(id)
            _friendState.value = result
        }
    }

    private val _qrResult = MutableStateFlow<String?>(null)
    val qrResult: StateFlow<String?> = _qrResult

    fun onQrScanned(id: String){
        _qrResult.value = id
    }

    fun clearResult(){
        _qrResult.value = null
    }


    //以下フレンド関連
    fun requestFriend(targetUserId: Int) {
        viewModelScope.launch {
            when (friendRepository.postFriendRequest(targetUserId)) {
                FriendRequestResult.Success -> {
                    _requestState.value = RequestResultEnum.Requested
                }
                FriendRequestResult.AlreadyFriend -> {
                    _requestState.value = RequestResultEnum.AlreadyFriend
                }
                FriendRequestResult.AlreadyRequested -> {
                    _requestState.value = RequestResultEnum.AlreadyRequested
                }
                else -> {
                    _requestState.value = RequestResultEnum.Error
                }

            }
        }
    }

}



class FriendAddViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory{
    override fun <T:ViewModel>create(modelClass:Class<T>):T{
        val api = RetrofitClient.userApi
        val friendApi = RetrofitClient.friendApi
        val repository = UserRepository(api, context.applicationContext)
        val friendRepository = FriendRepository(friendApi,context.applicationContext)
        return FriendAddViewModel(repository,friendRepository) as T
    }
}