package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.ExampleDataSource
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.domain.repository.ExampleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val exampleDataSource: ExampleDataSource
) : ExampleRepository {
    override suspend fun getExample(page: Int): Flow<List<UserEntity>> {
        return flow {
            val result = runCatching {
                exampleDataSource.getExample(page).data.map { it.toUserEntity() }
            }
            emit(result.getOrDefault(emptyList()))
        }
    }
}