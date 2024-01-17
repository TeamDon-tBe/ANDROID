package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestPostingDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PostingApiService {
    companion object {
        const val API = "api"
        const val V1 = "v1"
        const val CONTENT = "content"
    }

    @POST("/$API/$V1/$CONTENT")
    suspend fun posting(
        @Body requestPostingDto: RequestPostingDto,
    ): BaseResponse<Unit>
}
