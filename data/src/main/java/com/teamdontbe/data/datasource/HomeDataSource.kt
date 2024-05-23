package com.teamdontbe.data.datasource

import androidx.paging.PagingData
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface HomeDataSource {
    fun getFeedList(): Flow<PagingData<FeedEntity>>

    suspend fun getFeedDetail(contentId: Int): BaseResponse<ResponseFeedDto>

    fun getCommentList(contentId: Int): Flow<PagingData<CommentEntity>>

    suspend fun deleteFeed(contentId: Int): BaseResponse<Unit>

    suspend fun postFeedLiked(contentId: Int): BaseResponse<Unit>

    suspend fun deleteFeedLiked(contentId: Int): BaseResponse<Unit>

    suspend fun postCommentPosting(
        contentId: Int,
        commentText: RequestBody,
        image: MultipartBody.Part?
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
