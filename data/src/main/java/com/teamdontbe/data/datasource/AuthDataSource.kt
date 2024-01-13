package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.response.ResponseLoginDto

interface AuthDataSource {
    suspend fun login(auth:String, requestLogin: RequestLoginDto): BaseResponse<ResponseLoginDto>
}
