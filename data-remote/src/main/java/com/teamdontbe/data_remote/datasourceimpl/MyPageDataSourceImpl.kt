package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.MyPageDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import com.teamdontbe.data_remote.api.MyPageApiService
import javax.inject.Inject

class MyPageDataSourceImpl @Inject constructor(
    private val myPageUserProfileApiService: MyPageApiService,
) :
    MyPageDataSource {
    override suspend fun getMyPageUserProfileSource(viewMemberId: Int): BaseResponse<ResponseMyPageUserProfileDto> =
        myPageUserProfileApiService.getMyPageUserProfile(viewMemberId)

    override suspend fun getMyPageUserFeedListSource(viewMemberId: Int): BaseResponse<List<ResponseFeedDto>> =
        myPageUserProfileApiService.getMyPageFeedList(viewMemberId)
}
