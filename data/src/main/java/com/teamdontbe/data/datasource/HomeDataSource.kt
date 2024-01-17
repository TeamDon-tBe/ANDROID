package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestCommentPostingDto
import com.teamdontbe.data.dto.response.ResponseCommentDto
import com.teamdontbe.data.dto.response.ResponseFeedDto

interface HomeDataSource {
    suspend fun getFeedList(): BaseResponse<List<ResponseFeedDto>>

    suspend fun getFeedDetail(contentId: Int): BaseResponse<ResponseFeedDto>

    suspend fun getCommentList(contentId: Int): BaseResponse<List<ResponseCommentDto>>

    suspend fun deleteFeed(contentId: Int): BaseResponse<Unit>

    suspend fun postFeedLiked(contentId: Int): BaseResponse<Unit>

    suspend fun deleteFeedLiked(contentId: Int): BaseResponse<Unit>

    suspend fun postCommentPosting(
        contentId: Int,
        commentText: RequestCommentPostingDto,
    ): BaseResponse<Unit>
}
