package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getFeedList(): Flow<List<FeedEntity>?>

    suspend fun getFeedLDetail(contentId: Int): Flow<FeedEntity?>

    suspend fun getCommentList(contentId: Int): Flow<List<CommentEntity>?>
}
