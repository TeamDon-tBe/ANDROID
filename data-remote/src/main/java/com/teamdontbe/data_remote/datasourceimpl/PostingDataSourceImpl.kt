package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.PostingDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestPostingDto
import com.teamdontbe.data_remote.api.PostingApiService
import javax.inject.Inject

class PostingDataSourceImpl
    @Inject
    constructor(private val postingApiService: PostingApiService) :
    PostingDataSource {
        override suspend fun posting(requestPosting: RequestPostingDto): BaseResponse<Unit> = postingApiService.posting(requestPosting)
    }
