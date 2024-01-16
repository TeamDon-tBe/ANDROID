package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import kotlinx.coroutines.flow.Flow

interface MyPageUserProfileRepository {
    suspend fun getMyPageUserProfile(viewMemberId: Int): Flow<MyPageUserProfileEntity?>
}
