package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.LoginEntity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun postLogin(requestLogin: String): Flow<LoginEntity?>
}
