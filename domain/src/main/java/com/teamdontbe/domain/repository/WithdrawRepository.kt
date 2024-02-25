package com.teamdontbe.domain.repository

import kotlinx.coroutines.flow.Flow

interface WithdrawRepository {
    fun patchWithdraw(requestWithdraw: String): Flow<Boolean>
}
