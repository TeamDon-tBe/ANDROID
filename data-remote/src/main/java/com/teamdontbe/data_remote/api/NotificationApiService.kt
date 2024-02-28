package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseNotificationCountDto
import com.teamdontbe.data.dto.response.ResponseNotificationListDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface NotificationApiService {
    companion object {
        const val API = "api"
        const val V1 = "v1"
        const val NOTIFICATION = "notification"
        const val NUMBER = "number"
        const val NOTIFICATION_CHECK = "notification-check"
        const val MEMBER_NOTIFICATIONS = "member-notifications"
        const val CURSOR = "cursor"
    }

    @GET("/$API/$V1/$MEMBER_NOTIFICATIONS")
    suspend fun getNotificationList(
        @Query(value = CURSOR) notificationId: Long = -1,
    ): BaseResponse<List<ResponseNotificationListDto>>

    @GET("/$API/$V1/$NOTIFICATION/$NUMBER")
    suspend fun getNotificationCount(): BaseResponse<ResponseNotificationCountDto>

    @PATCH("/$API/$V1/$NOTIFICATION_CHECK")
    suspend fun patchNotificationCheck(): BaseResponse<Unit>
}
