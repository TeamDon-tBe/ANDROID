package com.teamdontbe.data_remote.pagingsourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data_remote.api.HomeApiService
import com.teamdontbe.domain.entity.FeedEntity
import timber.log.Timber

class HomePagingSourceImpl(private val homeApiService: HomeApiService) :
    PagingSource<Long, FeedEntity>() {

    companion object {
        var prevKey: Long? = null
        var refreshKey: Long = -1
    }

    override fun getRefreshKey(state: PagingState<Long, FeedEntity>): Long? {
        val anchorPosition = state.anchorPosition ?: return null
        prevKey = state.closestPageToPosition(anchorPosition)?.prevKey
//        prevKey.
//        Timber.tag("tttrefreshkey").d(prevKey.la)
        Timber.tag("position?").d(state.closestPageToPosition(anchorPosition).toString())
        return refreshKey
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, FeedEntity> {
        val position = params.key ?: -1
        refreshKey = position
        Timber.tag("tttposition").d(position.toString())
        Timber.tag("tttprevkey").d(prevKey.toString())
        return runCatching {
            val result = homeApiService.getFeedList(position)
            LoadResult.Page(
                // 매핑은 여기서
                data = result.data?.map { it.toFeedEntity() } ?: emptyList(),
                prevKey = prevKey,
                nextKey = if (result.data.isNullOrEmpty()) null else result.data?.last()?.contentId?.toLong(),
            ).also {
                prevKey = position
            }
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}
