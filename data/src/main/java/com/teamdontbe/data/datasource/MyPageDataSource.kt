package com.teamdontbe.data.datasource

import ResponseMyPageUserAccountInfoDto
import androidx.paging.PagingData
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseMyPageCommentDto
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow

interface MyPageDataSource {
    suspend fun getMyPageUserProfileSource(viewMemberId: Int): BaseResponse<ResponseMyPageUserProfileDto>

    fun getMyPageUserFeedListSource(viewMemberId: Int): Flow<PagingData<FeedEntity>>

    suspend fun getMyPageUserCommentListSource(viewMemberId: Int): BaseResponse<List<ResponseMyPageCommentDto>>

    suspend fun getMyPageUserAccountInfo(): BaseResponse<ResponseMyPageUserAccountInfoDto>
}
