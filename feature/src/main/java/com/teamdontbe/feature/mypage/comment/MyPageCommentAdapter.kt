package com.teamdontbe.feature.mypage.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.feature.databinding.ItemMyPageCommentBinding

class MyPageCommentAdapter(
    private val context: Context,
    private val idFlag: Boolean,
    private val onClickKebabBtn: (MyPageCommentEntity, Int) -> Unit,
    private val onItemClicked: (MyPageCommentEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickTransparentBtn: (MyPageCommentEntity) -> Unit,
) :
    PagingDataAdapter<MyPageCommentEntity, MyPageCommentViewHolder>(ExampleDiffCallback) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyPageCommentViewHolder {
        val binding = ItemMyPageCommentBinding.inflate(inflater, parent, false)
        return MyPageCommentViewHolder(
            binding = binding,
            onClickKebabBtn = onClickKebabBtn,
            onItemClicked = onItemClicked,
            onClickLikedBtn = onClickLikedBtn,
            idFlag = idFlag,
            onClickTransparentBtn = onClickTransparentBtn,
        )
    }

    override fun onBindViewHolder(
        holder: MyPageCommentViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { holder.onBind(it) }
    }

    fun deleteItem(position: Int) {
        notifyItemRemoved(position)
    }

    companion object {
        private val ExampleDiffCallback =
            ItemDiffCallback<MyPageCommentEntity>(
                onItemsTheSame = { old, new -> old.commentId == new.commentId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
