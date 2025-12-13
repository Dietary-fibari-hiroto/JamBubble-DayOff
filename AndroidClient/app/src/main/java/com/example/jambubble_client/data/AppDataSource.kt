package com.example.jambubble_client.data

import com.example.jambubble_client.data.dto.UserProfileDto
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    val userFlow: Flow<UserProfileDto>
    suspend fun save(user: UserProfileDto)
    suspend fun clear()
}

