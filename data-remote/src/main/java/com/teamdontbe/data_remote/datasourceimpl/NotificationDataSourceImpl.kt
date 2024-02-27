package com.teamdontbe.data_remote.datasourceimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teamdontbe.data.datasource.NotificationDataSource
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseNotificationCountDto
import com.teamdontbe.data_remote.api.NotificationApiService
import com.teamdontbe.data_remote.pagingsourceimpl.NotificationPagingSourceImpl
import com.teamdontbe.domain.entity.NotiEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationDataSourceImpl
    @Inject
    constructor(
        private val notificationApiService: NotificationApiService,
    ) : NotificationDataSource {
        override fun getNotificationList(): Flow<PagingData<NotiEntity>> {
            return Pager(PagingConfig(1)) {
                NotificationPagingSourceImpl(notificationApiService)
            }.flow
        }

        override suspend fun getNotificationCount(): BaseResponse<ResponseNotificationCountDto> =
            notificationApiService.getNotificationCount()

        override suspend fun patchNotificationCheck(): BaseResponse<Unit> = notificationApiService.patchNotificationCheck()
    }
