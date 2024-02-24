package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.WithdrawDataSource
import com.teamdontbe.data.dto.request.RequestWithdrawDto
import com.teamdontbe.domain.repository.WithdrawRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WithdrawRepositoryImpl
    @Inject
    constructor(
        private val withdrawDataSource: WithdrawDataSource,
    ) : WithdrawRepository {
        override suspend fun deleteWithdraw(withdrawalReason: String): Flow<Boolean> {
            return flow {
                val result =
                    runCatching {
                        withdrawDataSource.deleteWithdraw(RequestWithdrawDto(withdrawalReason)).success
                    }
                emit(result.getOrDefault(false))
            }
        }
    }
