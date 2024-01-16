package com.teamdontbe.domain.repository

import com.teamdontbe.domain.entity.NotiEntity
import kotlinx.coroutines.flow.Flow

interface NotificationListRepository {
    suspend fun getNotificationList(): Flow<List<NotiEntity>?>

    suspend fun getNotificationCount(): Flow<Int?>
}
