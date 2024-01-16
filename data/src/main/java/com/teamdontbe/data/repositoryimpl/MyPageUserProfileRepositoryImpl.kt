package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.MyPageUserProfileDataSource
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.repository.MyPageUserProfileDomainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageUserProfileRepositoryImpl
@Inject constructor(
    private val myPageUserProfileDataSource: MyPageUserProfileDataSource,
) : MyPageUserProfileDomainRepository {
    override suspend fun getMyPageUserProfile(viewMemberId: Int): Flow<MyPageUserProfileEntity?> {
        return flow {
            val result = kotlin.runCatching {
                myPageUserProfileDataSource.getMyPageUserProfileSource(viewMemberId).data?.toMyPageUserProfileEntity()
            }
            emit(result.getOrDefault(MyPageUserProfileEntity(0, "", "", "", 0)))
        }
    }

    override suspend fun getMyPageFeedList(viewMemberId: Int): Flow<List<FeedEntity>?> {
        return flow {
            val result = kotlin.runCatching {
                myPageUserProfileDataSource.getMyPageUserFeedListSource(viewMemberId).data?.map {
                    it.toFeedEntity()
                }
            }
            emit(
                result.getOrDefault(result.getOrDefault(emptyList())),
            )
        }
    }
}
