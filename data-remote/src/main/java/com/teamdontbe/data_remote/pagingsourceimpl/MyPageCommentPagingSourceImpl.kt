package com.teamdontbe.data_remote.pagingsourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data_remote.api.MyPageApiService
import com.teamdontbe.domain.entity.MyPageCommentEntity

class MyPageCommentPagingSourceImpl(
    private val myPageApiService: MyPageApiService,
    private val memberId: Int
) : PagingSource<Long, MyPageCommentEntity>() {

    private var prevKey: Long? = null

    companion object {
        var refreshKey: MutableList<Pair<Long, Long?>> = mutableListOf()
    }

    override fun getRefreshKey(state: PagingState<Long, MyPageCommentEntity>): Long? {
        return state.anchorPosition?.let { position ->
            prevKey = state.closestPageToPosition(position)?.prevKey
            refreshKey.find { it.second == state.closestPageToPosition(position)?.prevKey }?.first
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, MyPageCommentEntity> {
        val position = params.key ?: -1
        if (!refreshKey.any { it.first == position }) refreshKey.add(
            Pair(
                position,
                prevKey
            )
        )

        return runCatching {
            val result = myPageApiService.getMyPageCommentList(memberId, position)
            LoadResult.Page(
                // 매핑은 여기서
                data = result.data?.map { it.toMyPageCommentEntity() } ?: emptyList(),
                prevKey = refreshKey.find { it.first == position }?.second,
                nextKey = if (result.data.isNullOrEmpty()) null else result.data?.last()?.commentId?.toLong(),
            ).also {
                prevKey = position
            }
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}
