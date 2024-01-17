package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestLoginDto
import com.teamdontbe.data.dto.request.RequestProfileEditDto
import com.teamdontbe.data.dto.response.ResponseLoginDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApiService {
    companion object {
        const val API = "api"
        const val V1 = "v1"
        const val AUTH = "auth"
        const val NICKNAME_VALIDATION = "nickname-validation"
        const val NICKNAME = "nickname"
        const val USER_PROFILE = "user-profile"
    }

    @POST("/$API/$V1/$AUTH")
    suspend fun postLogin(
        @Body requestLogin: RequestLoginDto,
    ): BaseResponse<ResponseLoginDto>

    @GET("/$API/$V1/$NICKNAME_VALIDATION")
    suspend fun nickNameDoubleCheck(
        @Query(NICKNAME) nickname: String,
    ): BaseResponse<Unit>

    @PATCH("/$API/$V1/$USER_PROFILE")
    suspend fun patchUserProfile(
        @Body requestProfileEdit: RequestProfileEditDto,
    ): BaseResponse<Unit>
}
