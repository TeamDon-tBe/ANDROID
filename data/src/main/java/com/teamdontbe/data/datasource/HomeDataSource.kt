package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto

interface HomeDataSource {
    suspend fun getFeedList(): BaseResponse<List<ResponseFeedDto>>
}
