package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.response.ExampleListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ExampleApiService {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") api_key: String = "3cf034e13e35bd0801460d5558760b97",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): ExampleListResponseDto
}