package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.request.RequestProfileEditDto
import com.teamdontbe.data.dto.response.ResponseLoginDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface LoginDataSource {
    suspend fun postLogin(requestLogin: RequestLoginDto): BaseResponse<ResponseLoginDto>

    suspend fun getNickNameDoubleCheck(nickname: String): BaseResponse<Unit>

    suspend fun patchProfileEdit(requestProfileEdit: RequestProfileEditDto): BaseResponse<Unit>

    suspend fun patchUserProfilePart(
        info: RequestBody,
        file: MultipartBody.Part?
    ): BaseResponse<Unit>
}
