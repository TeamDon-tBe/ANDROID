package com.teamdontbe.data_remote.pagingsourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data_remote.api.HomeApiService
import com.teamdontbe.domain.entity.FeedEntity

class HomeFeedPagingSourceImpl(private val homeApiService: HomeApiService) :
    PagingSource<Long, FeedEntity>() {
    override fun getRefreshKey(state: PagingState<Long, FeedEntity>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            //contentId가 cursor -> 현재 페이지의 마지막 아이템 contentId를 key로 반환
            state.closestPageToPosition(anchorPosition)?.data?.last()?.contentId?.toLong() ?: -1
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, FeedEntity> {
        val position = params.key ?: -1
        return runCatching {
            val result = homeApiService.getFeedList(position)
            LoadResult.Page(
                //매핑은 여기서
                data = result.data?.map { it.toFeedEntity() } ?: emptyList(),
                prevKey = if (position.toInt() == -1) null else result.data?.first()?.contentId?.toLong(),
                nextKey = if (result.data.isNullOrEmpty()) null else result.data?.last()?.contentId?.toLong(),
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}