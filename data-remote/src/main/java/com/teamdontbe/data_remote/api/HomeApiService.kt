package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data_remote.api.LoginApiService.Companion.API
import com.teamdontbe.data_remote.api.LoginApiService.Companion.V1
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApiService {
    companion object {
        const val CONTENT = "content"
        const val ALL = "all"
        const val DETAIL = "detail"
    }

    @GET("/$API/$V1/$CONTENT/$ALL")
    suspend fun getFeedList(): BaseResponse<List<ResponseFeedDto>>

    @GET("/$API/$V1/$CONTENT/{contentId}/$DETAIL")
    suspend fun getFeedDetail(
        @Path(value = "contentId") contentId: Int,
    ): BaseResponse<ResponseFeedDto>
}
