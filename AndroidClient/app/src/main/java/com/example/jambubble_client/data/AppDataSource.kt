package com.example.jambubble_client.data


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jambubble_client.data.dto.UserProfileDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface UserLocalDataSource {
    val userFlow: Flow<UserProfileDto?>
    suspend fun save(user: UserProfileDto)
    suspend fun clear()
}


// DataStoreを使った実装例
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

object UserLocalDataStore : UserLocalDataSource {
    private lateinit var dataStore: DataStore<Preferences>
    private val USER_KEY = stringPreferencesKey("user_profile")

    fun initialize(context: Context) {
        dataStore = context.dataStore  // 上で定義した拡張プロパティを使用
    }

    //DataStore自体の初期化
    override val userFlow: Flow<UserProfileDto?>
        get() = dataStore.data
            .map { preferences ->
                preferences[USER_KEY]?.let { json ->
                    Json.decodeFromString<UserProfileDto>(json)
                }
            }
            .catch { emit(null) }


    override suspend fun save(user: UserProfileDto) {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = Json.encodeToString(user)
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}