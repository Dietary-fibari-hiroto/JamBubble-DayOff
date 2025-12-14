package com.example.jambubble_client.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.data.api.RetrofitClient
import com.example.jambubble_client.data.repository.AuthState
import com.example.jambubble_client.data.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BrandViewModel(
    private val userRepository: UserRepository
): ViewModel(){
    val authState: StateFlow<AuthState> = userRepository.authState

    init{
        viewModelScope.launch{
            userRepository.initialize()
        }
    }
}

class BrandViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val api = RetrofitClient.userApi
        val repository = UserRepository(api, context.applicationContext)

        return BrandViewModel(repository) as T
    }
}