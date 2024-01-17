package com.teamdontbe.data.datasource

import ResponseMyPageUserAccountInfoDto
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data.dto.response.ResponseMyPageCommentDto
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto

interface MyPageDataSource {
    suspend fun getMyPageUserProfileSource(viewMemberId: Int): BaseResponse<ResponseMyPageUserProfileDto>

    suspend fun getMyPageUserFeedListSource(viewMemberId: Int): BaseResponse<List<ResponseFeedDto>>

    suspend fun getMyPageUserCommentListSource(viewMemberId: Int): BaseResponse<List<ResponseMyPageCommentDto>>

    suspend fun getMyPageUserAccountInfo(): BaseResponse<ResponseMyPageUserAccountInfoDto>
}
