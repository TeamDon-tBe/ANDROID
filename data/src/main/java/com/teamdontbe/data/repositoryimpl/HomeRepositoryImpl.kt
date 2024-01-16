package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.HomeDataSource
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
                        homeDataSource.getFeedDetail(contentId)
                    }
            }
        }
    }
