package com.teamdontbe.feature.mypage.comment

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemCommentBinding

class MyPageCommentViewHolder(
    private val binding: ItemCommentBinding,
    private val onClickKebabBtn: (FeedEntity) -> Unit,
    private val onItemClicked: (FeedEntity) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    private var item: FeedEntity? = null

    init {
        binding.root.setOnClickListener {
            item?.let { onItemClicked(it) }
        }
        binding.btnCommentKebab.setOnClickListener {
            item?.let { onClickKebabBtn(it) }
        }
    }

    fun onBind(data: FeedEntity) = with(binding) {
        feed = data
        item = data
        executePendingBindings()
    }
}
