package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.AuthEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(requestLogin: String): Flow<AuthEntity?>
}
