package com.teamdontbe.data.datasource

import ResponseMyPageUserAccountInfoDto
import androidx.paging.PagingData
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow

interface MyPageDataSource {
    suspend fun getMyPageUserProfileSource(viewMemberId: Int): BaseResponse<ResponseMyPageUserProfileDto>

    fun getMyPageUserFeedListSource(viewMemberId: Int): Flow<PagingData<FeedEntity>>

    fun getMyPageUserCommentListSource(viewMemberId: Int): Flow<PagingData<CommentEntity>>

    suspend fun getMyPageUserAccountInfo(): BaseResponse<ResponseMyPageUserAccountInfoDto>
}
