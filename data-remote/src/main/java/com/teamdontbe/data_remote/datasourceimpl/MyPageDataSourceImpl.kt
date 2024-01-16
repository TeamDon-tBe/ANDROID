package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.MyPageDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data.dto.response.ResponseMyPageCommentDto
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import com.teamdontbe.data_remote.api.MyPageApiService
import javax.inject.Inject

class MyPageDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService,
) :
    MyPageDataSource {
    override suspend fun getMyPageUserProfileSource(viewMemberId: Int): BaseResponse<ResponseMyPageUserProfileDto> =
        myPageApiService.getMyPageUserProfile(viewMemberId)

    override suspend fun getMyPageUserFeedListSource(viewMemberId: Int): BaseResponse<List<ResponseFeedDto>> =
        myPageApiService.getMyPageFeedList(viewMemberId)

    override suspend fun getMyPageUserCommentListSource(viewMemberId: Int): BaseResponse<List<ResponseMyPageCommentDto>> =
        myPageApiService.getMyPageCommentList(viewMemberId)
}