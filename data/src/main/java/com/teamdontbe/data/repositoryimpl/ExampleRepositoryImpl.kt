package com.teamdontbe.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teamdontbe.data.datasource.ExampleDataSource
import com.teamdontbe.data_remote.api.ExampleApiService
import com.teamdontbe.data_remote.datasourceimpl.PagingSourceImpl
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.domain.repository.ExampleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExampleRepositoryImpl
    @Inject
    constructor(
        private val exampleDataSource: ExampleDataSource,
        private val apiService: ExampleApiService,
    ) : ExampleRepository {
        override fun getExample(page: Int): Flow<PagingData<UserEntity>> =
            Pager(PagingConfig(10)) {
                PagingSourceImpl(apiService)
            }.flow
    }
