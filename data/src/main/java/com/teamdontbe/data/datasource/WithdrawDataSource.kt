package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestWithdrawDto

interface WithdrawDataSource {
    suspend fun patchWithdraw(requestWithdraw: RequestWithdrawDto): BaseResponse<Unit>
}
