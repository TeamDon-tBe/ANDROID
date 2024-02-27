package com.teamdontbe.data.datasource

import androidx.paging.PagingData
import com.teamdontbe.data.dto.BaseResponse
import com.teamdontbe.data.dto.response.ResponseNotificationCountDto
import com.teamdontbe.domain.entity.NotiEntity
import kotlinx.coroutines.flow.Flow

interface NotificationDataSource {
    fun getNotificationList(): Flow<PagingData<NotiEntity>>

    suspend fun getNotificationCount(): BaseResponse<ResponseNotificationCountDto>

    suspend fun patchNotificationCheck(): BaseResponse<Unit>
}
