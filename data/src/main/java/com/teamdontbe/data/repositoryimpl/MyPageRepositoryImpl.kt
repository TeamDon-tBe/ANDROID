package com.teamdontbe.data.repositoryimpl

import androidx.paging.PagingData
import com.teamdontbe.data.datasource.MyPageDataSource
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.MyPageUserAccountInfoEntity
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import com.teamdontbe.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl
@Inject constructor(
    private val myPageDataSource: MyPageDataSource,
) : MyPageRepository {
    override suspend fun getMyPageUserProfile(viewMemberId: Int): Result<MyPageUserProfileEntity?> =
        runCatching {
            myPageDataSource.getMyPageUserProfileSource(viewMemberId).data?.toMyPageUserProfileEntity()
        }

    override fun getMyPageFeedList(viewMemberId: Int): Flow<PagingData<FeedEntity>> =
        myPageDataSource.getMyPageUserFeedListSource(viewMemberId)

    override fun getMyPageCommentList(viewMemberId: Int): Flow<PagingData<CommentEntity>> =
        myPageDataSource.getMyPageUserCommentListSource(viewMemberId)

    override suspend fun getMyPageUserAccountInfo(): Flow<MyPageUserAccountInfoEntity?> {
        return flow {
            val result = kotlin.runCatching {
                myPageDataSource.getMyPageUserAccountInfo().data?.toMyPageUserAccountInfoEntity()
            }
            emit(
                result.getOrDefault(
                    MyPageUserAccountInfoEntity(
                        "",
                        -1,
                        "",
                        "",
                        "",
                    ),
                ),
            )
        }
    }
}
