package com.teamdontbe.data_remote.datasourceimpl

import com.teamdontbe.data.datasource.NotificationListDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseNotificationCountDto
import com.teamdontbe.data.dto.response.ResponseNotificationListDto
import com.teamdontbe.data_remote.api.NotificationApiService
import javax.inject.Inject

class NotificationListDataSourceImpl
    @Inject
    constructor(
        private val notificationApiService: NotificationApiService,
    ) : NotificationListDataSource {
        override suspend fun getNotificationList(): BaseResponse<List<ResponseNotificationListDto>> =
            notificationApiService.getNotificationList()

        override suspend fun getNotificationCount(): BaseResponse<ResponseNotificationCountDto> =
            notificationApiService.getNotificationCount()
    }
