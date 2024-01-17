package com.teamdontbe.domain.repository

import kotlinx.coroutines.flow.Flow

interface PostingRepository {
    suspend fun posting(requestPosting: String): Flow<Boolean>
}
