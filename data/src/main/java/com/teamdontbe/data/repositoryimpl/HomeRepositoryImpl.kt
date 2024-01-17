package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.HomeDataSource
import com.teamdontbe.data.dto.request.RequestCommentPostingDto
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl
    @Inject
    constructor(
        private val homeDataSource: HomeDataSource,
    ) : HomeRepository {
        override suspend fun getFeedList(): Flow<List<FeedEntity>?> {
            return flow {
                val result =
                    runCatching {
                        homeDataSource.getFeedList().data?.map { it.toFeedEntity() }
                    }
                emit(result.getOrDefault(emptyList()))
            }
        }

        override suspend fun getFeedLDetail(contentId: Int): Flow<FeedEntity?> {
            return flow {
                val result =
                    runCatching {
                        homeDataSource.getFeedDetail(contentId).data?.toFeedEntity()
                    }
                emit(result.getOrDefault(FeedEntity(-1, "", "", false, false, -1, -1, -1, "", "")))
            }
        }

        override suspend fun getCommentList(contentId: Int): Flow<List<CommentEntity>?> {
            return flow {
                val result =
                    runCatching {
                        homeDataSource.getCommentList(contentId).data?.map { it.toCommentEntity() }
                    }
                emit(result.getOrDefault(emptyList()))
            }
        }

        override suspend fun deleteFeed(contentId: Int): Flow<Boolean> {
            return flow {
                val result =
                    runCatching {
                        homeDataSource.deleteFeed(contentId).success
                    }
                emit(result.getOrDefault(false))
            }
        }

        override suspend fun postFeedLiked(contentId: Int): Flow<Boolean> {
            return flow {
                val result =
                    runCatching {
                        homeDataSource.postFeedLiked(contentId).success
                    }
                emit(result.getOrDefault(false))
            }
        }

        override suspend fun deleteFeedLiked(contentId: Int): Flow<Boolean> {
            return flow {
                val result =
                    runCatching {
                        homeDataSource.deleteFeedLiked(contentId).success
                    }
                emit(result.getOrDefault(false))
            }
        }

        override suspend fun postCommentPosting(
            contentId: Int,
            commentText: String,
        ): Flow<Boolean> {
            return flow {
                val result =
                    runCatching {
                        homeDataSource.postCommentPosting(
                            contentId,
                            RequestCommentPostingDto(commentText),
                        ).success
                    }
                emit(result.getOrDefault(false))
            }
        }

        override suspend fun deleteComment(commentId: Int): Flow<Boolean> {
            return flow {
                val result =
                    runCatching {
                        homeDataSource.deleteComment(
                            commentId,
                        ).success
                    }
                emit(result.getOrDefault(false))
            }
        }
    }
