package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.ExampleDataSource
import com.teamdontbe.domain.entity.ExampleListEntity
import com.teamdontbe.domain.repository.ExampleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExampleRepositoryImpl  @Inject constructor(
    private val exampleDataSource: ExampleDataSource
) : ExampleRepository {
    override suspend fun getExample(page: Int): Flow<ExampleListEntity> {
        return flow{
            val result = kotlin.runCatching {
                exampleDataSource.getExample(page).results.map { it.toExampleEntity() }
            }
            emit(result.getOrDefault(ExampleListEntity(1, emptyList())) as ExampleListEntity)
        }
    }
}