package com.teamdontbe.feature.homedetail.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeCommentBinding

class HomeDetailViewHolder(
    private val binding: ItemHomeCommentBinding,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FeedEntity) {
        with(binding) {
            clCommentCard.isEnabled = false
            ivCommentGhostFillGreen.isVisible = true
            dividerComment.isVisible = true
            feed = data
            executePendingBindings()
            btnCommentKebab.setOnClickListener {
                onClickKebabBtn(data, bindingAdapterPosition)
            }
        }
    }
}
