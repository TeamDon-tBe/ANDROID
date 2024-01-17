package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.LoginDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.request.RequestProfileEditDto
import com.teamdontbe.data.dto.response.ResponseLoginDto
import com.teamdontbe.data_remote.api.LoginApiService
import javax.inject.Inject

class LoginDataSourceImpl
@Inject
constructor(
    private val loginApiService: LoginApiService,
) : LoginDataSource {
    override suspend fun postLogin(requestLogin: RequestLoginDto): BaseResponse<ResponseLoginDto> =
        loginApiService.postLogin(requestLogin)

    override suspend fun getNickNameDoubleCheck(nickname: String): BaseResponse<Unit> =
        loginApiService.nickNameDoubleCheck(nickname)

    override suspend fun patchProfileEdit(requestProfileEdit: RequestProfileEditDto): BaseResponse<Unit> =
        loginApiService.patchUserProfile(requestProfileEdit)
}
