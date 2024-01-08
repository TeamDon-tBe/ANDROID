package com.teamdontbe.data_remote.datasourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data_remote.api.ExampleApiService
import com.teamdontbe.domain.entity.UserEntity

class PagingSourceImpl(private val exampleApiService: ExampleApiService) :
    PagingSource<Int, UserEntity>() {
    override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? {
        return state.anchorPosition?.let { position ->
            // prevKey = null 첫번째, next = null 마지
            state.closestPageToPosition(position)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                position,
            )?.nextKey?.minus(1)
        }
    }

    // position, size 변경해주기
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> {
        val position = params.key ?: 1
        return runCatching {
            val result = exampleApiService.getListUsers(position)
            LoadResult.Page(
                data = result.data.map { it.toUserEntity() },
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (result.data.isEmpty()) null else position + 1,
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}
