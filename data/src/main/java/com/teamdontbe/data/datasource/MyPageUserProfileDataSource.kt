package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto

interface MyPageUserProfileDataSource {
    suspend fun getMyPageUserProfileSource(viewMemberId: Int): BaseResponse<ResponseMyPageUserProfileDto>

    suspend fun getMyPageUserFeedListSource(viewMemberId: Int): BaseResponse<List<ResponseFeedDto>>
}
