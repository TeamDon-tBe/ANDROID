package com.teamdontbe.data_remote.datasourceimpl

import ResponseMyPageUserAccountInfoDto
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teamdontbe.data.datasource.MyPageDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseMyPageCommentDto
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import com.teamdontbe.data_remote.api.MyPageApiService
import com.teamdontbe.data_remote.pagingsourceimpl.MyPagePagingSourceImpl
import com.teamdontbe.domain.entity.FeedEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyPageDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService,
) : MyPageDataSource {
    override suspend fun getMyPageUserProfileSource(viewMemberId: Int): BaseResponse<ResponseMyPageUserProfileDto> =
        myPageApiService.getMyPageUserProfile(viewMemberId)

    override fun getMyPageUserFeedListSource(viewMemberId: Int): Flow<PagingData<FeedEntity>> {
        return Pager(PagingConfig(1)) {
            MyPagePagingSourceImpl(myPageApiService, viewMemberId)
        }.flow
    }

    override suspend fun getMyPageUserCommentListSource(viewMemberId: Int): BaseResponse<List<ResponseMyPageCommentDto>> =
        myPageApiService.getMyPageCommentList(viewMemberId)

    override suspend fun getMyPageUserAccountInfo(): BaseResponse<ResponseMyPageUserAccountInfoDto> =
        myPageApiService.getMyPageUserAccountInfo()
}
