package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.LoginEntity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun login(requestLogin: String): Flow<LoginEntity?>
}
