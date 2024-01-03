package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.response.UserDataDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ExampleApiService {
    companion object {
        const val API = "api"
        const val USERS = "users"
        const val PAGE = "page"
    }

    @GET("/$API/$USERS")
    suspend fun getListUsers(
        @Query(PAGE) page: Int
    ): UserDataDto
}