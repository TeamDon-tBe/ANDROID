package com.teamdontbe.data_remote.pagingsourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data_remote.api.NotificationApiService
import com.teamdontbe.domain.entity.NotiEntity

class NotificationPagingSourceImpl(private val notificationApiService: NotificationApiService) :
    PagingSource<Long, NotiEntity>() {
    override fun getRefreshKey(state: PagingState<Long, NotiEntity>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.data?.last()?.notificationId?.toLong()
                ?: -1
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, NotiEntity> {
        val position = params.key ?: -1
        return runCatching {
            val result = notificationApiService.getNotificationList(position)
            LoadResult.Page(
                data = result.data?.map { it.toNotificationEntity() } ?: emptyList(),
                prevKey = if (position.toInt() == -1) null else result.data?.first()?.notificationId?.toLong(),
                nextKey = if (result.data.isNullOrEmpty()) null else result.data?.last()?.notificationId?.toLong(),
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}
