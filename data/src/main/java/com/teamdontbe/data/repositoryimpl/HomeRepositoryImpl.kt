package com.teamdontbe.data.repositoryimpl

import androidx.paging.PagingData
import com.teamdontbe.data.datasource.HomeDataSource
import com.teamdontbe.data.dto.request.RequestCommentPostingDto
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl
@Inject constructor(
    private val homeDataSource: HomeDataSource,
) : HomeRepository {
    override fun getFeedList(): Flow<PagingData<FeedEntity>> = homeDataSource.getFeedList()

    override suspend fun getFeedLDetail(contentId: Int): Result<FeedEntity?> {
        return runCatching {
            homeDataSource.getFeedDetail(contentId).data?.toFeedEntity()
        }
    }

    override fun getCommentList(contentId: Int): Flow<PagingData<CommentEntity>> = homeDataSource.getCommentList(contentId)

    override suspend fun deleteFeed(contentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.deleteFeed(contentId).success
        }
    }

    override suspend fun postFeedLiked(contentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.postFeedLiked(contentId).success
        }
    }

    override suspend fun deleteFeedLiked(contentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.deleteFeedLiked(contentId).success
        }
    }

    override suspend fun postCommentPosting(
        contentId: Int,
        commentText: String,
    ): Result<Boolean> {
        return runCatching {
            homeDataSource.postCommentPosting(
                contentId,
                RequestCommentPostingDto(commentText, "comment"),
            ).success
        }
    }

    override suspend fun deleteComment(commentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.deleteComment(
                commentId,
            ).success
        }
    }

    override suspend fun postCommentLiked(commentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.postCommentLiked(commentId).success
        }
    }

    override suspend fun deleteCommentLiked(commentId: Int): Result<Boolean> {
        return runCatching {
            homeDataSource.deleteCommentLiked(commentId).success
        }
    }

    override suspend fun postTransparent(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int,
        ghostReason: String
    ): Result<Boolean> {
        return runCatching {
            homeDataSource.postTransparent(
                alarmTriggerType,
                targetMemberId,
                alarmTriggerId,
                ghostReason
            ).success
        }
    }
}
