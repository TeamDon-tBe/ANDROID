package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.ExampleListEntity
import kotlinx.coroutines.flow.Flow

interface ExampleRepository {
    suspend fun getExample(page: Int): Flow<ExampleListEntity>
}