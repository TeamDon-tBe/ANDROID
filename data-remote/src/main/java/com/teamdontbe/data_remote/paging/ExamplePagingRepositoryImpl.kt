package com.teamdontbe.data_remote.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teamdontbe.data_remote.api.ExampleApiService
import com.teamdontbe.data_remote.datasourceimpl.PagingSourceImpl
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.domain.repository.ExamplePagingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExamplePagingRepositoryImpl
    @Inject
    constructor(
        private val apiService: ExampleApiService,
    ) : ExamplePagingRepository {
        override fun getPagingExample(): Flow<PagingData<UserEntity>> =
            Pager(PagingConfig(6)) {
                PagingSourceImpl(apiService)
            }.flow
    }
