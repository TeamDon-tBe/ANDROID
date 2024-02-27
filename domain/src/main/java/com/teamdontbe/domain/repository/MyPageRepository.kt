package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.domain.entity.MyPageUserAccountInfoEntity
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    suspend fun getMyPageUserProfile(viewMemberId: Int): Result<MyPageUserProfileEntity?>

    suspend fun getMyPageFeedList(viewMemberId: Int): Flow<List<FeedEntity>?>

    suspend fun getMyPageCommentList(viewMemberId: Int): Flow<List<MyPageCommentEntity>?>

    suspend fun getMyPageUserAccountInfo(): Flow<MyPageUserAccountInfoEntity?>
}
