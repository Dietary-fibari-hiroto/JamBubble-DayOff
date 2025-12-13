package com.example.jambubble_client.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.jambubble_client.Config
import com.example.jambubble_client.data.UserLocalDataSource
import com.example.jambubble_client.data.api.ApiConfig
import com.example.jambubble_client.data.api.service.UserApiService
import com.example.jambubble_client.data.dto.UserProfileDto
import com.example.jambubble_client.data.dto.UserRegisterDto
import com.example.jambubble_client.util.SecureStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class UserRepository(
    private val api: UserApiService,
    private val context: Context,  //ContentResolverに必要
    private val local: UserLocalDataSource
) {
    val authState = MutableStateFlow<AuthState>(AuthState.Loading)

    val userState: StateFlow<UserProfileDto?> =
        local.userFlow.stateIn(
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    suspend fun refreshUser(){
        val token = SecureStorage.load(context,Config.ACCESS_TOKEN)
        if(token == null){
            authState.value = AuthState.Unauthenticated
            return
        }
        val user = api.getUser("Bearer $token")
        Log.d("TAG", "ユーザー情報取得成功 $user")
        local.save(user)
    }

    suspend fun logout(){
        local.clear()
    }


    suspend fun initialize(){
        val token = SecureStorage.load(context,Config.ACCESS_TOKEN)
        if(token == null){
            authState.value = AuthState.Unauthenticated
            return
        }

        runCatching {
            api.getUser("Bearer $token")
        }.onSuccess{
            authState.value = AuthState.Authenticated(it)
        }.onFailure {
            SecureStorage.remove(context,Config.ACCESS_TOKEN)
            authState.value = AuthState.Unauthenticated
        }
    }
    //ユーザー登録処理
    suspend fun userRegister(dto: UserRegisterDto): Result<Unit> {
        return try {
            //StringをRequestBodyに変換
            val name = dto.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val email = dto.email.toRequestBody("text/plain".toMediaTypeOrNull())
            val password = dto.password.toRequestBody("text/plain".toMediaTypeOrNull())
            val gender = dto.gender.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val birthday = dto.birthday.toRequestBody("text/plain".toMediaTypeOrNull())

            // 画像を MultipartBody.Part に変換
            val imagePart = dto.userImage?.let { uri ->
                uriToMultipartBodyPart(uri, "userImage")
            }

            SecureStorage.save(context, ApiConfig.INIT_LOGIN_PASS_SPATH, dto.password)
            SecureStorage.save(context, ApiConfig.INIT_LOGIN_EMAIL_SPATH, dto.email)

            val response = api.postUser(
                name = name,
                email = email,
                password = password,
                gender = gender,
                birthday = birthday,
                userImage = imagePart
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("登録に失敗しました: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




    //UriをMultipartBody.Partに変換する関数
    private fun uriToMultipartBodyPart(uri: Uri, paramName: String): MultipartBody.Part? {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val fileName = getFileName(uri) ?: "image.jpg"
            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"

            val requestBody = inputStream.readBytes().toRequestBody(
                mimeType.toMediaTypeOrNull()
            )

            MultipartBody.Part.createFormData(paramName, fileName, requestBody)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    //ファイル名をゲットする関数
    private fun getFileName(uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) it.getString(nameIndex) else null
            } else null
        }
    }


}