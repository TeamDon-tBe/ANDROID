package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.LoginEntity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun postLogin(requestLogin: String): Flow<LoginEntity?>

    suspend fun getNickNameDoubleCheck(nickName: String): Flow<Boolean>

    suspend fun patchProfileEdit(
        nickName: String,
        allowed: Boolean?,
        intro: String,
        url: String?,
    ): Flow<Boolean>
}
