package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.PostingDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestPostingDto
import com.teamdontbe.data_remote.api.PostingApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PostingDataSourceImpl @Inject constructor(
    private val postingApiService: PostingApiService
) : PostingDataSource {
    override suspend fun posting(requestPosting: RequestPostingDto): BaseResponse<Unit> =
        postingApiService.posting(requestPosting)

    override suspend fun postingMultiPart(
        text: RequestBody,
        image: MultipartBody.Part?
    ): BaseResponse<Unit> {
        return postingApiService.postingMultiPart(text, image)
    }
}
