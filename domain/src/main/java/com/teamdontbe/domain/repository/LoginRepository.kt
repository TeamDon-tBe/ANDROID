package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.LoginEntity
import com.teamdontbe.domain.entity.ProfileEditInfoEntity
import kotlinx.coroutines.flow.Flow
import java.io.File

interface LoginRepository {
    suspend fun postLogin(requestLogin: String): Result<LoginEntity?>

    suspend fun getNickNameDoubleCheck(nickName: String): Flow<Boolean>

    suspend fun patchProfileEdit(
        nickName: String,
        allowed: Boolean?,
        intro: String,
        url: String?,
    ): Flow<Boolean>

    suspend fun patchProfileUriEdit(
        info: ProfileEditInfoEntity,
        file: File?
    ): Result<Boolean>
}
