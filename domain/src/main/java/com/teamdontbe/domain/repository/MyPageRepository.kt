package com.teamdontbe.domain.repository

import androidx.paging.PagingData
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.MyPageUserAccountInfoEntity
import com.teamdontbe.domain.entity.MyPageUserProfileEntity
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    suspend fun getMyPageUserProfile(viewMemberId: Int): Result<MyPageUserProfileEntity?>

    fun getMyPageFeedList(viewMemberId: Int): Flow<PagingData<FeedEntity>>

    fun getMyPageCommentList(viewMemberId: Int): Flow<PagingData<CommentEntity>>

    suspend fun getMyPageUserAccountInfo(): Flow<MyPageUserAccountInfoEntity?>
}
