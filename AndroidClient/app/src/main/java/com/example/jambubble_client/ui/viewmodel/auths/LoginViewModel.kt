package com.example.jambubble_client.ui.viewmodel.auths

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.data.api.RetrofitClient
import com.example.jambubble_client.data.repository.AuthRepository
import kotlinx.coroutines.launch

sealed interface LoginUiState{
    object Idle: LoginUiState
    object Loading : LoginUiState
    object Success: LoginUiState
    data class Error(val message:String): LoginUiState
}



class LoginViewModel(
    private val repository: AuthRepository
): ViewModel() {
    var uiState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState = LoginUiState.Loading

            val result = repository.login(email, password)

            uiState = result.fold(
                onSuccess = { LoginUiState.Success },
                onFailure = {
                    LoginUiState.Error(it.message ?: "ログイン失敗")
                }
            )
        }
    }
}


class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val api = RetrofitClient.authApi
        val repository = AuthRepository(api, context.applicationContext)

        return LoginViewModel(repository) as T
    }
}