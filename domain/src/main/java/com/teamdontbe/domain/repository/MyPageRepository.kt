package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    suspend fun getMyPageUserProfile(viewMemberId: Int): Flow<MyPageUserProfileEntity?>

    suspend fun getMyPageFeedList(viewMemberId: Int): Flow<List<FeedEntity>?>
}
