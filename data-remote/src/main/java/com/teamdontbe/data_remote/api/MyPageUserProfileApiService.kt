package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import com.teamdontbe.data_remote.api.LoginApiService.Companion.API
import com.teamdontbe.data_remote.api.LoginApiService.Companion.V1
import retrofit2.http.GET
import retrofit2.http.Path

interface MyPageUserProfileApiService {
    companion object {
        const val VIEW_MEMBER = "viewmember"
        const val VIEW_MEMBER_ID = "viewmemberId"
        const val MEMBER = "member"
        const val CONTENTS = "contents"
    }

    @GET("/$API/$V1/$VIEW_MEMBER/{$VIEW_MEMBER_ID}")
    suspend fun getMyPageUserProfile(
        @Path(VIEW_MEMBER_ID) viewMemberID: Int,
    ): BaseResponse<ResponseMyPageUserProfileDto>

    @GET("/$API/$V1/$MEMBER/{$VIEW_MEMBER_ID}/$CONTENTS")
    suspend fun getMyPageFeedList(
        @Path(VIEW_MEMBER_ID) viewMemberID: Int,
    ): BaseResponse<List<ResponseFeedDto>>
}
