package com.teamdontbe.data_remote.datasourceimpl

import ResponseMyPageUserAccountInfoDto
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teamdontbe.data.datasource.MyPageDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import com.teamdontbe.data_remote.api.MyPageApiService
import com.teamdontbe.data_remote.pagingsourceimpl.MyPageCommentPagingSourceImpl
import com.teamdontbe.data_remote.pagingsourceimpl.MyPageFeedPagingSourceImpl
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.MyPageCommentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyPageDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService,
) : MyPageDataSource {
    override suspend fun getMyPageUserProfileSource(viewMemberId: Int): BaseResponse<ResponseMyPageUserProfileDto> =
        myPageApiService.getMyPageUserProfile(viewMemberId)

    override fun getMyPageUserFeedListSource(viewMemberId: Int): Flow<PagingData<FeedEntity>> {
        return Pager(PagingConfig(1)) {
            MyPageFeedPagingSourceImpl(myPageApiService, viewMemberId)
        }.flow
    }

    override fun getMyPageUserCommentListSource(viewMemberId: Int): Flow<PagingData<MyPageCommentEntity>> {
        return Pager(PagingConfig(1)) {
            MyPageCommentPagingSourceImpl(myPageApiService, viewMemberId)
        }.flow
    }

    override suspend fun getMyPageUserAccountInfo(): BaseResponse<ResponseMyPageUserAccountInfoDto> =
        myPageApiService.getMyPageUserAccountInfo()
}
