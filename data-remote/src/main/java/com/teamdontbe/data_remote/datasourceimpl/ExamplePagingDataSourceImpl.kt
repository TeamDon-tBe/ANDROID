package com.teamdontbe.data_remote.datasourceimpl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamdontbe.data.dto.response.ExampleResponseDto
import com.teamdontbe.data_remote.api.ExampleApiService
import javax.inject.Inject

class ExamplePagingDataSourceImpl @Inject constructor(
    private val exampleApiService: ExampleApiService
) : PagingSource<Int, ExampleResponseDto>() {
    override fun getRefreshKey(state: PagingState<Int, ExampleResponseDto>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                position
            )?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExampleResponseDto> {
        val position = params.key ?: 1
        return runCatching {
            val result = exampleApiService.getTopRatedMovies(page = position)
            LoadResult.Page(
                data = result.results,
                prevKey = if (position == 0) null else position - 1,
                nextKey = position + 1
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}