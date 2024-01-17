package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseNotificationCountDto
import com.teamdontbe.data.dto.response.ResponseNotificationListDto
import retrofit2.http.GET
import retrofit2.http.PATCH

interface NotificationApiService {
    companion object {
        const val API = "api"
        const val V1 = "v1"
        const val NOTIFICATION_ALL = "notification-all"
        const val NOTIFICATION = "notification"
        const val NUMBER = "number"
        const val NOTIFICATION_CHECK = "notification-check"
    }

    @GET("/$API/$V1/$NOTIFICATION_ALL")
    suspend fun getNotificationList(): BaseResponse<List<ResponseNotificationListDto>>

    @GET("/$API/$V1/$NOTIFICATION/$NUMBER")
    suspend fun getNotificationCount(): BaseResponse<ResponseNotificationCountDto>

    @PATCH("/$API/$V1/$NOTIFICATION_CHECK")
    suspend fun patchNotificationCheck(): BaseResponse<Unit>
}
