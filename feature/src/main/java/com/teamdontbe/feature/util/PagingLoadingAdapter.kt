package com.teamdontbe.feature.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.feature.databinding.ItemPagingLoadingBinding

class PagingLoadingAdapter : LoadStateAdapter<PagingLoadingAdapter.PagingLoadingViewHolder>() {

    class PagingLoadingViewHolder(
        private val binding: ItemPagingLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.pbPagingLoading.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: PagingLoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadingViewHolder {
        val binding =
            ItemPagingLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingLoadingViewHolder(binding)
    }
}