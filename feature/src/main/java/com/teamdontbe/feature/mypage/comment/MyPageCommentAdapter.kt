package com.teamdontbe.feature.mypage.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.feature.databinding.ItemMyPageCommentBinding

class MyPageCommentAdapter(
    private val onClickKebabBtn: (MyPageCommentEntity) -> Unit,
    private val onItemClicked: (MyPageCommentEntity) -> Unit,
    context: Context,
) :
    ListAdapter<MyPageCommentEntity, MyPageCommentViewHolder>(ExampleDiffCallback) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageCommentViewHolder {
        val binding = ItemMyPageCommentBinding.inflate(inflater, parent, false)
        return MyPageCommentViewHolder(binding, onClickKebabBtn, onItemClicked)
    }

    override fun onBindViewHolder(holder: MyPageCommentViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        private val ExampleDiffCallback =
            ItemDiffCallback<MyPageCommentEntity>(
                onItemsTheSame = { old, new -> old.memberId == new.memberId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
