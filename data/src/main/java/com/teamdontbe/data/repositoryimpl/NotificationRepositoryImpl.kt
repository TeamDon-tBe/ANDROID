package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.NotificationDataSource
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepositoryImpl
    @Inject
    constructor(
        private val notificationDataSource: NotificationDataSource,
    ) : NotificationRepository {
        override suspend fun getNotificationList(): Flow<List<NotiEntity>?> {
            return flow {
                val result =
                    runCatching {
                        notificationDataSource.getNotificationList().data?.map { it.toNotificationEntity() }
                    }
                emit(result.getOrDefault(emptyList()))
            }
        }

        override suspend fun getNotificationCount(): Flow<Int?> {
            return flow {
                val result =
                    runCatching {
                        notificationDataSource.getNotificationCount().data?.notificationNumber
                    }
                emit(result.getOrDefault(-1))
            }
        }
    }
