package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.MyPageDataSource
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl
@Inject constructor(
    private val myPageDataSource: MyPageDataSource,
) : MyPageRepository {
    override suspend fun getMyPageUserProfile(viewMemberId: Int): Flow<MyPageUserProfileEntity?> {
        return flow {
            val result = kotlin.runCatching {
                myPageDataSource.getMyPageUserProfileSource(viewMemberId).data?.toMyPageUserProfileEntity()
            }
            emit(result.getOrDefault(MyPageUserProfileEntity(-1, "", "", "", -1)))
        }
    }

    override suspend fun getMyPageFeedList(viewMemberId: Int): Flow<List<FeedEntity>?> {
        return flow {
            val result = kotlin.runCatching {
                myPageDataSource.getMyPageUserFeedListSource(viewMemberId).data?.map {
                    it.toFeedEntity()
                }
            }
            emit(
                result.getOrDefault(result.getOrDefault(emptyList())),
            )
        }
    }

    override suspend fun getMyPageCommentList(viewMemberId: Int): Flow<List<MyPageCommentEntity>?> {
        return flow {
            val result = kotlin.runCatching {
                myPageDataSource.getMyPageUserCommentListSource(viewMemberId).data?.map {
                    it.toMyPageCommentEntity()
                }
            }
            emit(
                result.getOrDefault(result.getOrDefault(emptyList())),
            )
        }
    }
}
