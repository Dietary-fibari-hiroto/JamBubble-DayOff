package com.example.jambubble_client.ui.viewmodel.auths

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.data.api.ApiConfig
import com.example.jambubble_client.data.api.RetrofitClient
import com.example.jambubble_client.data.repository.AuthRepository
import com.example.jambubble_client.util.SecureStorage
import kotlinx.coroutines.launch

sealed interface InitialLoginState {
    object Idle : InitialLoginState
    object Loading : InitialLoginState
    object Success : InitialLoginState
    data class Error(val message: String): InitialLoginState
}

class InitialLoginViewModel(
    private val repository: AuthRepository,
    private val context: Context
): ViewModel(){
    var uiState by mutableStateOf<InitialLoginState>(InitialLoginState.Idle)
        private set

    fun initialLogin(){
        viewModelScope.launch{
            uiState = InitialLoginState.Loading

            val pass = SecureStorage.load(context, ApiConfig.INIT_LOGIN_PASS_SPATH)
            val email = SecureStorage.load(context, ApiConfig.INIT_LOGIN_EMAIL_SPATH)

            if(pass == null && email == null){
                InitialLoginState.Error("ストレージからデータを取得できませんでした。")
            }
            val result = repository.login(email!!,pass!!)

            uiState = result.fold(
                onSuccess = {
                    SecureStorage.remove(context, ApiConfig.INIT_LOGIN_PASS_SPATH)
                    SecureStorage.remove(context, ApiConfig.INIT_LOGIN_EMAIL_SPATH)
                    InitialLoginState.Success
                            },
                onFailure = {
                    InitialLoginState.Error(it.message ?: "ログイン失敗")
                }
            )
        }
    }
}


class InitialLoadingViewModelFactory(
    private val context:Context
): ViewModelProvider.Factory{
    override fun <T:ViewModel> create(modelClass: Class<T>):T{
        val api = RetrofitClient.authApi
        val repository = AuthRepository(api,context.applicationContext)

        return InitialLoginViewModel(repository,context) as T
    }

}