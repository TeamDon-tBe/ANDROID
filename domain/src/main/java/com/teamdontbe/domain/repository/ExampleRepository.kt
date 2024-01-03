package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface ExampleRepository {
    suspend fun getExample(page: Int): Flow<List<UserEntity>>
}