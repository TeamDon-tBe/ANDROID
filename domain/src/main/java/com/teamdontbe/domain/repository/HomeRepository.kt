package com.teamdontbe.domain.repository

import androidx.paging.PagingData
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getFeedList(): Flow<PagingData<FeedEntity>>

    suspend fun getFeedLDetail(contentId: Int): Result<FeedEntity?>

    fun getCommentList(contentId: Int): Flow<PagingData<CommentEntity>>

    suspend fun deleteFeed(contentId: Int): Result<Boolean>

    suspend fun postFeedLiked(contentId: Int): Result<Boolean>

    suspend fun deleteFeedLiked(contentId: Int): Result<Boolean>

    suspend fun postCommentPosting(
        contentId: Int,
        commentText: String,
        uriString: String?
    ): Result<Boolean>

    suspend fun deleteComment(commentId: Int): Result<Boolean>

    suspend fun postCommentLiked(commentId: Int): Result<Boolean>

    suspend fun deleteCommentLiked(commentId: Int): Result<Boolean>

    suspend fun postTransparent(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int,
        ghostReason: String
    ): Result<Boolean>

    suspend fun postComplaint(
        reportTargetNickname: String,
        relateText: String
    ): Result<Boolean>
}
