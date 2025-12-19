package com.example.jambubble_client.ui.viewmodel.auths

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.data.api.RetrofitClient
import com.example.jambubble_client.data.dto.UserRegisterDto
import com.example.jambubble_client.data.repository.UserRepository
import kotlinx.coroutines.launch

sealed interface  RegisterConfirmUiState{
    object Idle : RegisterConfirmUiState
    object Loading : RegisterConfirmUiState
    object Success:RegisterConfirmUiState
    data class Error(val message:String):RegisterConfirmUiState
}

class RegisterConfirmViewmodel(
    private val repository: UserRepository
) : ViewModel() {
    var uiState by mutableStateOf<RegisterConfirmUiState>(RegisterConfirmUiState.Idle)
        private set

    fun register(
        name: String,
        email: String,
        password: String,
        gender: String,
        birthday: String,
        userImage: Uri?
    ) {
        viewModelScope.launch {
            uiState = RegisterConfirmUiState.Loading

            try {
                val dto = UserRegisterDto(
                    name = name,
                    email = email,
                    password = password,
                    gender = gender.toGenderInt(),
                    birthday = birthday,  // .toString() は不要
                    userImage = userImage  // Uri.EMPTY ではなく null
                )

                val result = repository.userRegister(dto)

                result.onSuccess {
                    uiState = RegisterConfirmUiState.Success
                }.onFailure { e ->
                    uiState = RegisterConfirmUiState.Error(
                        e.message ?: "登録に失敗しました。"
                    )
                }
            } catch (e: Exception) {
                uiState = RegisterConfirmUiState.Error(
                    e.message ?: "登録に失敗しました。"
                )
            }
        }
    }

    private fun String.toGenderInt(): Int = when (this) {
        "男性" -> 1
        "女性" -> 2
        else -> 0
    }
}

class RegisterConfirmViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterConfirmViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterConfirmViewmodel(
                UserRepository(RetrofitClient.userApi, context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}