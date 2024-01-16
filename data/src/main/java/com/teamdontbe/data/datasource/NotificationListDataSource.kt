package com.teamdontbe.data.datasource

import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseNotificationCountDto
import com.teamdontbe.data.dto.response.ResponseNotificationListDto

interface NotificationListDataSource {
    suspend fun getNotificationList(): BaseResponse<List<ResponseNotificationListDto>>

    suspend fun getNotificationCount(): BaseResponse<ResponseNotificationCountDto>
}
