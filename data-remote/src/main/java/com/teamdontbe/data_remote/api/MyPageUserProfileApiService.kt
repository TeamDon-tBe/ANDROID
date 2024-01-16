package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseMyPageFeedListDto
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MyPageUserProfileApiService {
    companion object {
        const val VIEW_MEMBER = "viewmember"
        const val MEMBER = "member"
    }

    @GET("/${LoginApiService.API}/${LoginApiService.V1}/$VIEW_MEMBER/{viewmemberId}")
    suspend fun getMyPageUserProfile(
        @Path("viewmemberId") viewMemberID: Int,
    ): BaseResponse<ResponseMyPageUserProfileDto>

    @GET("/${LoginApiService.API}/${LoginApiService.V1}/$MEMBER/{viewmemberId}/${HomeApiService.CONTENT}")
    suspend fun getMyPageFeedList(
        @Path("viewmemberId") viewMemberID: Int,
    ): BaseResponse<ResponseMyPageFeedListDto>
}
