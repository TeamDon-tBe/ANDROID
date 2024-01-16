package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseCommentDto
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data_remote.api.LoginApiService.Companion.API
import com.teamdontbe.data_remote.api.LoginApiService.Companion.V1
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApiService {
    companion object {
        const val CONTENT = "content"
        const val ALL = "all"
        const val COMMENT = "comment"
        const val DETAIL = "detail"
        const val CONTENT_ID = "contentId"
    }

    @GET("/$API/$V1/$CONTENT/$ALL")
    suspend fun getFeedList(): BaseResponse<List<ResponseFeedDto>>

    @GET("/$API/$V1/$CONTENT/{$CONTENT_ID}/$DETAIL")
    suspend fun getFeedDetail(
        @Path(value = CONTENT_ID) contentId: Int,
    ): BaseResponse<ResponseFeedDto>

    @GET("$API/$V1/$CONTENT/{$CONTENT_ID}/$COMMENT/$ALL")
    suspend fun getCommentList(
        @Path(value = CONTENT_ID) contentId: Int,
    ): BaseResponse<List<ResponseCommentDto>>
}
