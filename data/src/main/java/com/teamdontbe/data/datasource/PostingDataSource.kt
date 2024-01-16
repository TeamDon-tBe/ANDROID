package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestPostingDto

interface PostingDataSource {
    suspend fun posting(requestPostingDto: RequestPostingDto): BaseResponse<Unit>
}
