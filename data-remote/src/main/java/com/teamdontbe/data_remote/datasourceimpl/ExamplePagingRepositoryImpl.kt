package com.teamdontbe.data_remote.datasourceimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teamdontbe.data_remote.api.ExampleApiService
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.domain.repository.ExamplePagingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExamplePagingRepositoryImpl
    @Inject
    constructor(
        private val apiService: ExampleApiService,
    ) : ExamplePagingRepository {
        override fun getPagingExample(page: Int): Flow<PagingData<UserEntity>> =
            Pager(PagingConfig(page)) {
                PagingSourceImpl(apiService)
            }.flow
    }
