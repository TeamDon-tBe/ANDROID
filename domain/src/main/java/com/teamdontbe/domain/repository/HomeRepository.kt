package com.teamdontbe.domain.repository

import androidx.paging.PagingData
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getFeedList(): Flow<PagingData<FeedEntity>>

    suspend fun getFeedLDetail(contentId: Int): Flow<FeedEntity?>

    suspend fun getCommentList(contentId: Int): Flow<List<CommentEntity>?>

    suspend fun deleteFeed(contentId: Int): Flow<Boolean>

    suspend fun postFeedLiked(contentId: Int): Flow<Boolean>

    suspend fun deleteFeedLiked(contentId: Int): Flow<Boolean>

    suspend fun postCommentPosting(
        contentId: Int,
        commentText: String,
    ): Flow<Boolean>

    suspend fun deleteComment(commentId: Int): Flow<Boolean>

    suspend fun postCommentLiked(commentId: Int): Flow<Boolean>

    suspend fun deleteCommentLiked(commentId: Int): Flow<Boolean>

    suspend fun postTransparent(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int,
        ghostReason: String
    ): Result<Boolean>
}
