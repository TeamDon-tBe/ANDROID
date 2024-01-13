package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.AuthDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.response.ResponseLoginDto
import com.teamdontbe.data_remote.api.AuthApiService
import javax.inject.Inject

class AuthDataSourceImpl
    @Inject
    constructor(
        private val authApiService: AuthApiService,
    ) : AuthDataSource {
        override suspend fun login(auth: String, requestLogin: RequestLoginDto): BaseResponse<ResponseLoginDto> =
            authApiService.login(auth, requestLogin)
    }
