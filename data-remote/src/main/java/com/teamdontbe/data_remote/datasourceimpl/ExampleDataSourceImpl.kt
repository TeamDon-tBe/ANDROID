package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.ExampleDataSource
import com.teamdontbe.data.dto.response.UserDataDto
import com.teamdontbe.data_remote.api.ExampleApiService
import javax.inject.Inject

class ExampleDataSourceImpl @Inject constructor(
    private val exampleApiService: ExampleApiService
) : ExampleDataSource {
    override suspend fun getExample(page: Int): UserDataDto {
        return exampleApiService.getListUsers(page)
    }
}