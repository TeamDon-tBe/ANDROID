package com.teamdontbe.feature.mypage.comment

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.feature.databinding.ItemMyPageCommentBinding

class MyPageCommentViewHolder(
    private val binding: ItemMyPageCommentBinding,
    private val onClickKebabBtn: (MyPageCommentEntity) -> Unit,
    private val onItemClicked: (MyPageCommentEntity) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    private var item: MyPageCommentEntity? = null

    init {
        binding.root.setOnClickListener {
            item?.let { onItemClicked(it) }
        }
        binding.btnCommentKebab.setOnClickListener {
            item?.let { onClickKebabBtn(it) }
        }
    }

    fun onBind(data: MyPageCommentEntity) = with(binding) {
        feed = data
        item = data
        executePendingBindings()
    }
}
