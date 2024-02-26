package com.teamdontbe.data.repositoryimpl

import androidx.paging.PagingData
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
        override fun getNotificationList(): Flow<PagingData<NotiEntity>> = notificationDataSource.getNotificationList()

        override suspend fun getNotificationCount(): Flow<Int?> {
            return flow {
                val result =
                    runCatching {
                        notificationDataSource.getNotificationCount().data?.notificationNumber
                    }
                emit(result.getOrDefault(-1))
            }
        }

        override suspend fun patchNotificationCheck(): Flow<Boolean> {
            return flow {
                val result =
                    runCatching {
                        notificationDataSource.patchNotificationCheck().success
                    }
                emit(result.getOrDefault(false))
            }
        }
    }
