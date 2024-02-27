package com.teamdontbe.data_remote.pagingsourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data_remote.api.MyPageApiService
import com.teamdontbe.domain.entity.FeedEntity

class MyPagePagingSourceImpl(
    private val myPageApiService: MyPageApiService,
    private val memberId: Int
) :
    PagingSource<Long, FeedEntity>() {
    override fun getRefreshKey(state: PagingState<Long, FeedEntity>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.data?.last()?.contentId?.toLong() ?: -1
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, FeedEntity> {
        val position = params.key ?: -1
        return runCatching {
            val result = myPageApiService.getMyPageFeedList(memberId, position)
            LoadResult.Page(
                // 매핑은 여기서
                data = result.data?.map { it.toFeedEntity() } ?: emptyList(),
                prevKey = if (position.toInt() == -1) null else result.data?.first()?.contentId?.toLong(),
                nextKey = if (result.data.isNullOrEmpty()) null else result.data?.last()?.contentId?.toLong(),
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}
