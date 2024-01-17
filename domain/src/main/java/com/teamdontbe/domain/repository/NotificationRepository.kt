package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.NotiEntity
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getNotificationList(): Flow<List<NotiEntity>?>

    suspend fun getNotificationCount(): Flow<Int?>

    suspend fun patchNotificationCheck(): Flow<Boolean>
}
