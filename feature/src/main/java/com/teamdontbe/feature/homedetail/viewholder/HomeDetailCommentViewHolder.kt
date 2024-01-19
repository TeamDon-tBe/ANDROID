package com.teamdontbe.feature.homedetail.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.feature.databinding.ItemHomeCommentBinding
import com.teamdontbe.feature.util.CalculateTime
import com.teamdontbe.feature.util.Transparent
import timber.log.Timber

class HomeDetailCommentViewHolder(
    private val binding: ItemHomeCommentBinding,
    private val onClickKebabBtn: (CommentEntity, Int) -> Unit = { _, _ -> },
    private val onClickLikedBtn: (Int, Boolean) -> Unit = { _, _ -> },
    private val onClickTransparentBtn: (CommentEntity, Int) -> Unit = { _, _ -> },
    private val userId: Int,
    private val onClickUserProfileBtn: (CommentEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: CommentEntity,
        lastPosition: Int,
    ) {
        with(binding) {
            if (data.memberId == userId) {
                ivCommentGhostFillGreen.visibility = View.INVISIBLE
                dividerComment.visibility = View.INVISIBLE
            } else {
                ivCommentGhostFillGreen.visibility = View.VISIBLE
                dividerComment.visibility = View.VISIBLE
            }
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

            setTransparent(data)
            ivCommentProfile.setOnClickListener {
                onClickUserProfileBtn(data, bindingAdapterPosition)
            }
        }
    }

    private fun setTransparent(data: CommentEntity) {
        binding.ivCommentGhostFillGreen.setOnClickListener {
            if (!data.isGhost) {
                onClickTransparentBtn(data, position)
            } else {
                onClickTransparentBtn(
                    data,
                    -2,
                )
            }
        }

        if (data.isGhost) {
            binding.viewCommentTransparentBg.setBackgroundColor(Color.parseColor("#D9FCFCFD"))
        } else {
            val color = Transparent().calculateColorWithOpacity(data.memberGhost)
            Timber.tag("color").d(color)
            binding.viewCommentTransparentBg.setBackgroundColor(Color.parseColor(color))
        }
    }
}
