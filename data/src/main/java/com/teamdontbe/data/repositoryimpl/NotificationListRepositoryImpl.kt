package com.teamdontbe.data.repositoryimpl

import com.teamdontbe.data.datasource.NotificationListDataSource
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.domain.repository.NotificationListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationListRepositoryImpl
    @Inject
    constructor(
        private val notificationListDataSource: NotificationListDataSource,
    ) : NotificationListRepository {
        override suspend fun getNotificationList(): Flow<List<NotiEntity>?> {
            return flow {
                val result =
                    runCatching {
                        notificationListDataSource.getNotificationList().data?.map { it.toNotificationListEntity() }
                    }
                emit(result.getOrDefault(emptyList()))
            }
        }

        override suspend fun getNotificationCount(): Flow<Int?> {
            return flow {
                val result =
                    runCatching {
                        notificationListDataSource.getNotificationCount().data?.notificationNumber
                    }
                emit(result.getOrDefault(0))
            }
        }
    }
