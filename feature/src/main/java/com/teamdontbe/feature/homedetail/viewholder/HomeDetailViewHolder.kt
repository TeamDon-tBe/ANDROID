package com.teamdontbe.feature.homedetail.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding

class HomeDetailViewHolder(
    private val binding: ItemHomeFeedBinding,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FeedEntity) {
        with(binding) {
            clHomeFeedCard.isEnabled = false
            btnHomeComment.isVisible = bindingAdapterPosition == 0
            tvHomeCommentNum.isVisible = bindingAdapterPosition == 0
            feed = data
            executePendingBindings()
            btnHomeKebab.setOnClickListener {
                onClickKebabBtn(data, bindingAdapterPosition)
            }
        }
    }
}
