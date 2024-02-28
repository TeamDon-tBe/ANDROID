package com.teamdontbe.data_remote.pagingsourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data_remote.api.HomeApiService
import com.teamdontbe.domain.entity.CommentEntity
import timber.log.Timber

class HomeCommentPagingSourceImpl(
    private val homeApiService: HomeApiService,
    private val contentId: Int
) :
    PagingSource<Long, CommentEntity>() {

    private var prevKey: Long? = null

    companion object {
        var refreshKey: MutableList<Pair<Long, Long?>> = mutableListOf()
    }

    override fun getRefreshKey(state: PagingState<Long, CommentEntity>): Long? {
        return state.anchorPosition?.let { position ->
            prevKey = state.closestPageToPosition(position)?.prevKey
            refreshKey.find { it.second == state.closestPageToPosition(position)?.prevKey }?.first
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, CommentEntity> {
        val position = params.key ?: -1
        Timber.tag("tttposition").d(position.toString())
        if (!refreshKey.any { it.first == position }) refreshKey.add(
            Pair(
                position,
                prevKey
            )
        )
        return runCatching {
            val result = homeApiService.getCommentList(contentId, position)
            LoadResult.Page(
                data = result.data?.map { it.toCommentEntity() } ?: emptyList(),
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
