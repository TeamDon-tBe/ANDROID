package com.teamdontbe.data_remote.pagingsourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data_remote.api.MyPageApiService
import com.teamdontbe.domain.entity.FeedEntity

class MyPageFeedPagingSourceImpl(
    private val myPageApiService: MyPageApiService,
    private val memberId: Int
) : PagingSource<Long, FeedEntity>() {

    private var prevKey: Long? = null

    companion object {
        var refreshKey: MutableList<Pair<Long, Long?>> = mutableListOf()
    }

    override fun getRefreshKey(state: PagingState<Long, FeedEntity>): Long? {
        return state.anchorPosition?.let { position ->
            prevKey = state.closestPageToPosition(position)?.prevKey
            refreshKey.find { it.second == state.closestPageToPosition(position)?.prevKey }?.first
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, FeedEntity> {
        val position = params.key ?: -1
        if (!refreshKey.any { it.first == position }) refreshKey.add(
            Pair(
                position,
                prevKey
            )
        )

        return runCatching {
            val result = myPageApiService.getMyPageFeedList(memberId, position)
            LoadResult.Page(
                // 매핑은 여기서
                data = result.data?.map { it.toFeedEntity() } ?: emptyList(),
                prevKey = refreshKey.find { it.first == position }?.second,
                nextKey = if (result.data.isNullOrEmpty()) null else result.data?.last()?.contentId?.toLong(),
            ).also {
                prevKey = position
            }
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}
