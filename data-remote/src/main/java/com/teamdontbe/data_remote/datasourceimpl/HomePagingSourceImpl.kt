package com.teamdontbe.data_remote.datasourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data_remote.api.HomeApiService
import com.teamdontbe.domain.entity.FeedEntity

class HomePagingSourceImpl(private val homeApiService: HomeApiService) :
    PagingSource<Long, FeedEntity>() {
    override fun getRefreshKey(state: PagingState<Long, FeedEntity>): Long? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.contentId?.toLong()
            ?: -1
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, FeedEntity> {
        val position = params.key ?: -1
        return runCatching {
            val result = homeApiService.getFeedList(position)
            LoadResult.Page(
                data = result.data?.map { it.toFeedEntity() } ?: emptyList(),
                prevKey = if (position.toInt() == -1) null else position - 1,
                nextKey = if (result.data.isNullOrEmpty()) null else result.data?.last()?.contentId?.toLong(),
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}