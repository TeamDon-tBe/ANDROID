package com.teamdontbe.feature.homedetail.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.feature.databinding.ItemHomeCommentBinding
import com.teamdontbe.feature.util.CalculateTime

class HomeDetailCommentViewHolder(
    private val binding: ItemHomeCommentBinding,
    private val onClickKebabBtn: (CommentEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: CommentEntity,
        lastPosition: Int,
    ) {
        with(binding) {
            tvCommentTransparency.text = "투명도 + ${data.memberGhost}%  · ${
                CalculateTime(binding.root.context).getCalculateTime(data.time)
            }"
            comment = data
            executePendingBindings()
            btnCommentKebab.setOnClickListener {
                onClickKebabBtn(data, bindingAdapterPosition)
            }
            if (lastPosition == position) dividerCommentDivideBottom.isVisible = false
        }
    }
}
