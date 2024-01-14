package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.response.ResponseLoginDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    companion object {
        const val API = "api"
        const val V1 = "v1"
        const val AUTH = "auth"
    }

    @POST("/$API/$V1/$AUTH")
    suspend fun login(
        @Header("Authorization") auth: String,
        @Body requestLogin: RequestLoginDto,
    ): BaseResponse<ResponseLoginDto>
}
