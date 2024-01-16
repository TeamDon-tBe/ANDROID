package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.MyPageUserProfileDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import com.teamdontbe.data_remote.api.MyPageUserProfileApiService
import javax.inject.Inject

class MyPageUserProfileDataSourceImpl @Inject constructor(
    private val myPageUserProfileApiService: MyPageUserProfileApiService,
) :
    MyPageUserProfileDataSource {
    override suspend fun getMyPageUserProfile(viewMemberId: Int): BaseResponse<ResponseMyPageUserProfileDto> =
        myPageUserProfileApiService.getMyPageUserProfile(viewMemberId)
}
