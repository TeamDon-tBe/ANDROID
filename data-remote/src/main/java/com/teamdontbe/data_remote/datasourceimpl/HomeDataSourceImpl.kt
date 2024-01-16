package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.HomeDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data_remote.api.HomeApiService
import javax.inject.Inject

class HomeDataSourceImpl
    @Inject
    constructor(
        private val homeApiService: HomeApiService,
    ) : HomeDataSource {
        override suspend fun getFeedList(): BaseResponse<List<ResponseFeedDto>> {
            return homeApiService.getFeedList()
        }

        override suspend fun getFeedDetail(contentId: Int): BaseResponse<ResponseFeedDto> {
            return homeApiService.getFeedDetail(contentId)
        }
    }
