package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.request.RequestProfileEditDto
import com.teamdontbe.data.dto.response.ResponseLoginDto

interface LoginDataSource {
    suspend fun postLogin(requestLogin: RequestLoginDto): BaseResponse<ResponseLoginDto>

    suspend fun getNickNameDoubleCheck(nickname: String): BaseResponse<Unit>

    suspend fun patchProfileEdit(requestProfileEdit: RequestProfileEditDto): BaseResponse<Unit>
}
