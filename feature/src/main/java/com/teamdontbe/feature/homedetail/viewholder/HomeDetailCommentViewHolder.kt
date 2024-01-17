package com.teamdontbe.feature.homedetail.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.feature.databinding.ItemHomeCommentBinding
import com.teamdontbe.feature.util.CalculateTime

class HomeDetailCommentViewHolder(
    private val binding: ItemHomeCommentBinding,
    private val onClickKebabBtn: (CommentEntity, Int) -> Unit = { _, _ -> },
    private val onClickLikedBtn: (Int, Boolean) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: CommentEntity,
        lastPosition: Int,
    ) {
        with(binding) {
            if (data.time.isNotEmpty()) {
                tvCommentTransparency.text = "투명도 ${data.memberGhost}% · ${
                    CalculateTime(binding.root.context).getCalculateTime(data.time)
                }"
            }
            btnCommentHeart.isSelected = data.isLiked
            comment = data
            executePendingBindings()
            btnCommentKebab.setOnClickListener {
                onClickKebabBtn(data, bindingAdapterPosition)
            }
            btnCommentHeart.setOnClickListener {
                onClickLikedBtn(data.commentId, btnCommentHeart.isSelected)
                val likeNumber = tvCommentLikeNum.text.toString()
                tvCommentLikeNum.text =
                    if (btnCommentHeart.isSelected) (likeNumber.toInt() - 1).toString() else (likeNumber.toInt() + 1).toString()
                btnCommentHeart.isSelected = !btnCommentHeart.isSelected
            }
            if (lastPosition == position) dividerCommentDivideBottom.isVisible = false
        }
    }
}
