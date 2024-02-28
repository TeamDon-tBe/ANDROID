package com.teamdontbe.domain.repository

import androidx.paging.PagingData
import com.teamdontbe.domain.entity.NotiEntity
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotificationList(): Flow<PagingData<NotiEntity>>

    suspend fun getNotificationCount(): Flow<Int?>

    suspend fun patchNotificationCheck(): Flow<Boolean>
}
