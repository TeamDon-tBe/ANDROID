package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.response.ExampleListResponseDto

interface ExampleDataSource {

    suspend fun getExample(page: Int): ExampleListResponseDto
}