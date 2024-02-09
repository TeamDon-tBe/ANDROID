package com.teamdontbe.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.home.viewholder.HomeFeedViewHolder

class HomeFeedAdapter(
    private val userId: Int,
    private val onClickToNavigateToHomeDetail: (FeedEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickUserProfileBtn: (Int) -> Unit,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit,
    private val onClickTransparentBtn: (FeedEntity) -> Unit,
) : ListAdapter<FeedEntity, HomeFeedViewHolder>(
    HomeAdapterDiffCallback,
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HomeFeedViewHolder {
        val binding =
            ItemHomeFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeFeedViewHolder(
            binding,
            userId,
            onClickToNavigateToHomeDetail,
            onClickLikedBtn,
            onClickUserProfileBtn,
            onClickKebabBtn,
            onClickTransparentBtn
        )

    }

    override fun onBindViewHolder(
        holder: HomeFeedViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    fun deleteItem(position: Int) {
        submitList(currentList.toMutableList().apply { removeAt(position) })
    }

    companion object {
        private val HomeAdapterDiffCallback = ItemDiffCallback<FeedEntity>(
            onItemsTheSame = { old, new -> old.memberId == new.memberId },
            onContentsTheSame = { old, new -> old == new },
        )
    }
}
