package com.teamdontbe.data_remote.datasourceimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teamdontbe.data_remote.api.HomeApiService
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagingTest   @Inject
constructor(
    private val apiService: HomeApiService,
) : HomeRepository {

    override suspend fun getFeedList(): Flow<List<FeedEntity>?> {
        TODO("Not yet implemented")
    }

    override suspend fun getFeedPagingList(): Flow<PagingData<FeedEntity>?> {
        return Pager(PagingConfig(30)) {
            HomePagingSourceImpl(apiService)
        }.flow
    }

    override suspend fun getFeedLDetail(contentId: Int): Flow<FeedEntity?> {
        TODO("Not yet implemented")
    }

    override suspend fun getCommentList(contentId: Int): Flow<List<CommentEntity>?> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFeed(contentId: Int): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postFeedLiked(contentId: Int): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFeedLiked(contentId: Int): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postCommentPosting(contentId: Int, commentText: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(commentId: Int): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postCommentLiked(commentId: Int): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCommentLiked(commentId: Int): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun postTransparent(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int
    ): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}