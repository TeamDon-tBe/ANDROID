package com.teamdontbe.domain.repository

import androidx.paging.PagingData
import com.teamdontbe.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface ExampleRepository {
    fun getExample(page: Int): Flow<PagingData<UserEntity>>
}
