package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestPostingDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface PostingDataSource {
    suspend fun posting(requestPostingDto: RequestPostingDto): BaseResponse<Unit>
    suspend fun postingMultiPart(
        text: RequestBody,
        image: MultipartBody.Part?,
    ): BaseResponse<Unit>
}
