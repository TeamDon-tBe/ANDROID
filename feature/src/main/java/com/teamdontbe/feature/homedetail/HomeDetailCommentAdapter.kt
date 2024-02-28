package com.teamdontbe.feature.homedetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.feature.databinding.ItemHomeCommentBinding
import com.teamdontbe.feature.homedetail.viewholder.HomeDetailCommentViewHolder

class HomeDetailCommentAdapter(
    private val context: Context,
    private val onClickKebabBtn: (CommentEntity, Int) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickTransparentBtn: (CommentEntity) -> Unit,
    private val userId: Int,
    private val onClickUserProfileBtn: (Int) -> Unit,
) :
    ListAdapter<CommentEntity, HomeDetailCommentViewHolder>(
        HomeAdapterDiffCallback,
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HomeDetailCommentViewHolder {
        val binding =
            ItemHomeCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeDetailCommentViewHolder(
            context,
            binding,
            onClickKebabBtn,
            onClickLikedBtn,
            onClickTransparentBtn,
            userId,
            onClickUserProfileBtn,
        )
    }

    override fun onBindViewHolder(
        holder: HomeDetailCommentViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position], itemCount)
    }

    fun deleteItem(position: Int) {
        submitList(currentList.toMutableList().apply { removeAt(position) })
    }

    companion object {
        private val HomeAdapterDiffCallback =
            ItemDiffCallback<CommentEntity>(
                onItemsTheSame = { old, new -> old.memberId == new.memberId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
