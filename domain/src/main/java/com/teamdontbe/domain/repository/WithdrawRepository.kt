package com.teamdontbe.domain.repository

import kotlinx.coroutines.flow.Flow

interface WithdrawRepository {
    suspend fun deleteWithdraw(requestWithdraw: String): Flow<Boolean>
}
