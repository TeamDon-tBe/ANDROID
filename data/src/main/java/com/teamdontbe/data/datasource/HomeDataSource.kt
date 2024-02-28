package com.teamdontbe.data.datasource

import androidx.paging.PagingData
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestCommentPostingDto
import com.teamdontbe.data.dto.response.ResponseCommentDto
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow

interface HomeDataSource {
    fun getFeedList(): Flow<PagingData<FeedEntity>>

    suspend fun getFeedDetail(contentId: Int): BaseResponse<ResponseFeedDto>

    suspend fun getCommentList(contentId: Int): BaseResponse<List<ResponseCommentDto>>

    suspend fun deleteFeed(contentId: Int): BaseResponse<Unit>

    suspend fun postFeedLiked(contentId: Int): BaseResponse<Unit>

    suspend fun deleteFeedLiked(contentId: Int): BaseResponse<Unit>

    suspend fun postCommentPosting(
        contentId: Int,
        commentText: RequestCommentPostingDto,
    ): BaseResponse<Unit>

    suspend fun deleteComment(commentId: Int): BaseResponse<Unit>

    suspend fun postCommentLiked(commentId: Int): BaseResponse<Unit>

    suspend fun deleteCommentLiked(commentId: Int): BaseResponse<Unit>

    suspend fun postTransparent(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int,
        ghostReason: String
    ): BaseResponse<Unit>
}
