package com.example.jambubble_client.ui.viewmodel.friends

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.data.api.RetrofitClient
import com.example.jambubble_client.data.dto.OtherUserProfileDto
import com.example.jambubble_client.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendAddViewModel(
    private val userRepository: UserRepository
): ViewModel() {

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
}



class FriendAddViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory{
    override fun <T:ViewModel>create(modelClass:Class<T>):T{
        val api = RetrofitClient.userApi
        val repository = UserRepository(api, context.applicationContext)
        return FriendAddViewModel(repository) as T
    }
}