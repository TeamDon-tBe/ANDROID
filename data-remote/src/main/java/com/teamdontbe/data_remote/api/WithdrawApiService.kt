package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestWithdrawDto
import retrofit2.http.Body
import retrofit2.http.PATCH

interface WithdrawApiService {
    companion object {
        const val API = "api"
        const val V1 = "v1"
        const val WITHDRAWAL = "withdrawal"
    }

    @PATCH("/$API/$V1/$WITHDRAWAL")
    suspend fun patchWithdraw(
        @Body requestWithdraw: RequestWithdrawDto,
    ): BaseResponse<Unit>
}
