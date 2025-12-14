package com.example.jambubble_client.ui.viewmodel.users

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jambubble_client.data.api.RetrofitClient
import com.example.jambubble_client.data.dto.SessionListResponseDto
import com.example.jambubble_client.data.repository.SessionRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionRepository: SessionRepository
): ViewModel(){
    private val _uiState =
        MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState


    fun loadDataLists(){
        viewModelScope.launch{
            _uiState.value = MainUiState.Loading

            try{
                val favoriteDeferred = async {
                    sessionRepository.getFavoritePlaylist()
                }

                val friendDeferred = async {
                    sessionRepository.getFriendPlaylist()
                }


                val favoriteResult = favoriteDeferred.await()
                val friendResult = friendDeferred.await()

                if(favoriteResult.isSuccess && friendResult.isSuccess) {
                    _uiState.value = MainUiState.Success(
                        favoriteSessionList = favoriteResult.getOrThrow(),
                        friendSessionList = friendResult.getOrThrow()
                    )
                }else{
                    _uiState.value = MainUiState.Error("セッション一覧の取得に失敗しました。")
                }
            }catch(e:Exception){
                _uiState.value = MainUiState.Error("セッション一覧の取得に失敗しました。")
            }
        }
    }
}


sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(
        val favoriteSessionList: List<SessionListResponseDto>,
        val friendSessionList:List<SessionListResponseDto>)
        : MainUiState()
    data class Error(val message: String) : MainUiState()
}



class MainViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory{
    override fun <T:ViewModel>create(modelClass:Class<T>):T{
        val api = RetrofitClient.sessionApi
        val repository = SessionRepository(api, context = context)

        return MainViewModel(repository) as T

    }
}