package com.teamdontbe.feature.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.teamdontbe.feature.home.HomePagingFeedAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T : Any> pagingSubmitData(
    lifecycleOwner: LifecycleOwner,
    getData: Flow<PagingData<T>>,
    pagingAdapter: PagingDataAdapter<T, *>,
) {
    lifecycleOwner.lifecycleScope.launch {
        getData.collectLatest { pagingData ->
            pagingAdapter.submitData(pagingData)
        }
    }
}