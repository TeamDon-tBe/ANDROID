package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.LoginDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.response.ResponseLoginDto
import com.teamdontbe.data_remote.api.LoginApiService
import javax.inject.Inject

class LoginDataSourceImpl
    @Inject
    constructor(
        private val loginApiService: LoginApiService,
    ) : LoginDataSource {
        override suspend fun login(auth: String, requestLogin: RequestLoginDto): BaseResponse<ResponseLoginDto> =
            loginApiService.login(auth, requestLogin)
    }
