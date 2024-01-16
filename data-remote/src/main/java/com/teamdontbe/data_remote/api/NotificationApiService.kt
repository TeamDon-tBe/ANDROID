package com.teamdontbe.data_remote.api

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseNotificationListDto
import retrofit2.http.GET

interface NotificationApiService {
    companion object {
        const val API = "api"
        const val V1 = "v1"
        const val NOTIFICATION_ALL = "notification-all"
    }

    @GET("/$API/$V1/$NOTIFICATION_ALL")
    suspend fun getNotificationList(): BaseResponse<List<ResponseNotificationListDto>>
}
