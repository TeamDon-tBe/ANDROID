package com.teamdontbe.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.home.viewholder.HomeViewHolder

class HomeAdapter(
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
    private val onClickToNavigateToHomeDetail: (FeedEntity, Int) -> Unit = { _, _ -> },
    private val onClickLikedBtn: (Int, Boolean) -> Unit = { _, _ -> },
    private val onClickUserProfileBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
    private val onClickTransparentBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
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
        return HomeViewHolder(
            binding,
            onClickKebabBtn,
            onClickToNavigateToHomeDetail,
            onClickLikedBtn,
            onClickUserProfileBtn,
            onClickTransparentBtn,
        )
    }

    override fun onBindViewHolder(
        holder: HomeViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    fun deleteItem(position: Int) {
        submitList(currentList.toMutableList().apply { removeAt(position) })
    }

    fun updateItemAtPosition(
        position: Int,
        isGhost: Boolean,
    ) {
        val oldItem = currentList[position]
        val newItem = oldItem.copy(isGhost = isGhost)

        val mutableList = currentList.toMutableList()
        mutableList[position] = newItem
        submitList(mutableList)
    }

    companion object {
        private val HomeAdapterDiffCallback =
            ItemDiffCallback<FeedEntity>(
                onItemsTheSame = { old, new -> old.memberId == new.memberId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
