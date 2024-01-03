package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.response.UserDataDto

interface ExampleDataSource {
    suspend fun getExample(page: Int): UserDataDto
}