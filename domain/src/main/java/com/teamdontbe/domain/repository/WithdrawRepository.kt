package com.teamdontbe.domain.repository

import kotlinx.coroutines.flow.Flow

interface WithdrawRepository {
    suspend fun patchWithdraw(requestWithdraw: String): Flow<Boolean>
}
