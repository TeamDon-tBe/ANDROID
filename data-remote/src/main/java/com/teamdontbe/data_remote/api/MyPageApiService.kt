package com.teamdontbe.data_remote.api

import ResponseMyPageUserAccountInfoDto
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseFeedDto
import com.teamdontbe.data.dto.response.ResponseMyPageCommentDto
import com.teamdontbe.data.dto.response.ResponseMyPageUserProfileDto
import com.teamdontbe.data_remote.api.HomeApiService.Companion.CURSOR
import com.teamdontbe.data_remote.api.LoginApiService.Companion.API
import com.teamdontbe.data_remote.api.LoginApiService.Companion.V1
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyPageApiService {
    companion object {
        const val VIEW_MEMBER = "viewmember"
        const val VIEW_MEMBER_ID = "viewmemberId"
        const val MEMBER = "member"
        const val MEMBER_DATA = "member-data"
        const val MEMBER_ID = "memberId"
        const val MEMBER_CONTENTS = "member-contents"
        const val MEMBER_COMMENTS = "member-comments"
    }

    @GET("/$API/$V1/$VIEW_MEMBER/{$VIEW_MEMBER_ID}")
    suspend fun getMyPageUserProfile(
        @Path(VIEW_MEMBER_ID) viewMemberID: Int,
    ): BaseResponse<ResponseMyPageUserProfileDto>

    @GET("/$API/$V1/$MEMBER/{$MEMBER_ID}/$MEMBER_CONTENTS")
    suspend fun getMyPageFeedList(
        @Path(MEMBER_ID) viewMemberID: Int,
        @Query(CURSOR) cursor: Long = -1
    ): BaseResponse<List<ResponseFeedDto>>

    @GET("/$API/$V1/$MEMBER/{$MEMBER_ID}/$MEMBER_COMMENTS")
    suspend fun getMyPageCommentList(
        @Path(MEMBER_ID) viewMemberID: Int,
        @Query(CURSOR) cursor: Long = -1
    ): BaseResponse<List<ResponseMyPageCommentDto>>

    @GET("/$API/$V1/$MEMBER_DATA")
    suspend fun getMyPageUserAccountInfo(): BaseResponse<ResponseMyPageUserAccountInfoDto>
}
