package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.request.RequestPostingDto
import com.teamdontbe.data_remote.api.HomeApiService.Companion.V2
import com.teamdontbe.data_remote.api.LoginApiService.Companion.V1
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostingApiService {
    companion object {
        const val API = "api"
        const val CONTENT = "content"
    }

    @POST("/$API/$V1/$CONTENT")
    suspend fun posting(
        @Body requestPostingDto: RequestPostingDto,
    ): BaseResponse<Unit>

    @Multipart
    @POST("/$API/$V2/$CONTENT")
    suspend fun postingMultiPart(
        @Part("text") text: RequestBody,
        @Part image: MultipartBody.Part?,
    ): BaseResponse<Unit>
}
