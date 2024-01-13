package com.teamdontbe.feature.homedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeCommentBinding
import com.teamdontbe.feature.homedetail.viewholder.HomeDetailCommentViewHolder

class HomeDetailCommentAdapter(
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
) :
    ListAdapter<FeedEntity, HomeDetailCommentViewHolder>(
            HomeAdapterDiffCallback,
        ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HomeDetailCommentViewHolder {
        val binding =
            ItemHomeCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeDetailCommentViewHolder(binding, onClickKebabBtn)
    }

    override fun onBindViewHolder(
        holder: HomeDetailCommentViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position], itemCount)
    }

    companion object {
        private val HomeAdapterDiffCallback =
            ItemDiffCallback<FeedEntity>(
                onItemsTheSame = { old, new -> old.memberId == new.memberId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}