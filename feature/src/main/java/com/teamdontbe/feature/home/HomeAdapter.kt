package com.teamdontbe.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.home.viewholder.HomeViewHolder

class HomeAdapter(
    private val click: (FeedEntity, Int) -> Unit = { _, _ -> },
) :
    ListAdapter<FeedEntity, HomeViewHolder>(
            HomeAdapterDiffCallback,
        ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HomeViewHolder {
        val binding =
            ItemHomeFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding, click)
    }

    override fun onBindViewHolder(
        holder: HomeViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    companion object {
        private val HomeAdapterDiffCallback =
            ItemDiffCallback<FeedEntity>(
                onItemsTheSame = { old, new -> old.memberId == new.memberId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
