package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.WithdrawDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestWithdrawDto
import com.teamdontbe.data_remote.api.WithdrawApiService
import javax.inject.Inject

class WithdrawDataSourceImpl
    @Inject
    constructor(
        private val withdrawApiService: WithdrawApiService,
    ) : WithdrawDataSource {
        override suspend fun deleteWithdraw(requestWithdraw: RequestWithdrawDto): BaseResponse<Unit> =
            withdrawApiService.deleteWithdraw(requestWithdraw)
    }
