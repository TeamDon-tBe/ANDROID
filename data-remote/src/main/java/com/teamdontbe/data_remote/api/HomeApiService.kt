package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestCommentLikedDto
import com.teamdontbe.data.dto.request.RequestCommentPostingDto
import com.teamdontbe.data.dto.request.RequestFeedLikedDto
import com.teamdontbe.data.dto.request.RequestTransparentDto
import com.teamdontbe.data.dto.response.ResponseCommentDto
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data.dto.response.ResponseFeedV2Dto
import com.teamdontbe.data_remote.api.LoginApiService.Companion.API
import com.teamdontbe.data_remote.api.LoginApiService.Companion.V1
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApiService {
    companion object {
        const val CONTENT = "content"
        const val CONTENTS = "contents"
        const val CURSOR = "cursor"
        const val ALL = "all"
        const val COMMENT = "comment"
        const val DETAIL = "detail"
        const val CONTENT_ID = "contentId"
        const val LIKED = "liked"
        const val UNLIKED = "unliked"
        const val COMMENT_ID = "commentId"
        const val GHOST = "ghost"
    }

    @GET("$API/$V1/$CONTENTS")
    suspend fun getFeedList(
        @Query(value = CURSOR) contentId: Long = -1,
    ): BaseResponse<List<ResponseFeedV2Dto>>

    @GET("/$API/$V1/$CONTENT/{$CONTENT_ID}/$DETAIL")
    suspend fun getFeedDetail(
        @Path(value = CONTENT_ID) contentId: Int,
    ): BaseResponse<ResponseFeedDto>

    @GET("$API/$V1/$CONTENT/{$CONTENT_ID}/$COMMENT/$ALL")
    suspend fun getCommentList(
        @Path(value = CONTENT_ID) contentId: Int,
    ): BaseResponse<List<ResponseCommentDto>>

    @DELETE("/$API/$V1/$CONTENT/{$CONTENT_ID}")
    suspend fun deleteFeed(
        @Path(value = CONTENT_ID) contentId: Int,
    ): BaseResponse<Unit>

    @POST("/$API/$V1/$CONTENT/{$CONTENT_ID}/$LIKED")
    suspend fun postLiked(
        @Path(value = CONTENT_ID) contentId: Int,
        @Body request: RequestFeedLikedDto = RequestFeedLikedDto("contentLiked"),
    ): BaseResponse<Unit>

    @DELETE("/$API/$V1/$CONTENT/{$CONTENT_ID}/$UNLIKED")
    suspend fun deleteLiked(
        @Path(value = CONTENT_ID) contentId: Int,
    ): BaseResponse<Unit>

    @POST("/$API/$V1/$CONTENT/{$CONTENT_ID}/$COMMENT")
    suspend fun postCommentPosting(
        @Path(value = CONTENT_ID) contentId: Int,
        @Body request: RequestCommentPostingDto,
    ): BaseResponse<Unit>

    @DELETE("/$API/$V1/$COMMENT/{$COMMENT_ID}")
    suspend fun deleteComment(
        @Path(value = COMMENT_ID) commentId: Int,
    ): BaseResponse<Unit>

    @POST("/$API/$V1/$COMMENT/{$COMMENT_ID}/$LIKED")
    suspend fun postCommentLiked(
        @Path(value = COMMENT_ID) commentId: Int,
        @Body request: RequestCommentLikedDto,
    ): BaseResponse<Unit>

    @DELETE("/$API/$V1/$COMMENT/{$COMMENT_ID}/$UNLIKED")
    suspend fun deleteCommentLiked(
        @Path(value = COMMENT_ID) commentId: Int,
    ): BaseResponse<Unit>

    @POST("/$API/$V1/$GHOST")
    suspend fun postTransparent(
        @Body request: RequestTransparentDto,
    ): BaseResponse<Unit>
}
