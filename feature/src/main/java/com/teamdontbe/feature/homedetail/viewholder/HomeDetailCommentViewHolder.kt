package com.teamdontbe.feature.homedetail.viewholder

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.feature.databinding.ItemHomeCommentBinding
import com.teamdontbe.feature.util.Transparent

class HomeDetailCommentViewHolder(
    private val binding: ItemHomeCommentBinding,
    private val onClickKebabBtn: (CommentEntity, Int) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickTransparentBtn: (CommentEntity) -> Unit,
    private val userId: Int,
    private val onClickUserProfileBtn: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: CommentEntity,
        lastPosition: Int,
    ) {
        with(binding) {
            comment = data
            executePendingBindings()

            ivCommentGhostFillGreen.isVisible = data.memberId !== userId
            dividerComment.isVisible = data.memberId !== userId
            btnCommentHeart.isSelected = data.isLiked

            if (lastPosition == position) dividerCommentDivideBottom.isVisible = false
            if (data.isGhost) setFeedTransparent(-85) else setFeedTransparent(data.memberGhost)

            initLikedBtnCLickListener(data)
            initProfileBtnClickListener(data)
            initKebabBtnClickListener(data)
            initGhostBtnClickListener(data)
        }
    }

    private fun initProfileBtnClickListener(data: CommentEntity) {
        binding.ivCommentProfile.setOnClickListener {
            onClickUserProfileBtn(data.memberId)
        }
    }

    private fun initLikedBtnCLickListener(data: CommentEntity) {
        with(binding) {
            btnCommentHeart.setOnClickListener {
                onClickLikedBtn(data.commentId, btnCommentHeart.isSelected)
                val likeNumber = tvCommentLikeNum.text.toString()
                tvCommentLikeNum.text =
                    if (btnCommentHeart.isSelected) (likeNumber.toInt() - 1).toString() else (likeNumber.toInt() + 1).toString()
                btnCommentHeart.isSelected = !btnCommentHeart.isSelected
            }
        }
    }

    private fun initKebabBtnClickListener(data: CommentEntity) {
        binding.btnCommentKebab.setOnClickListener {
            onClickKebabBtn(data, bindingAdapterPosition)
        }
    }

    private fun initGhostBtnClickListener(data: CommentEntity) {
        binding.ivCommentGhostFillGreen.setOnClickListener {
            onClickTransparentBtn(data)
        }
    }

    private fun setFeedTransparent(memberGhostPercent: Int) {
        val color = Transparent().calculateColorWithOpacity(memberGhostPercent)
        binding.viewCommentTransparentBg.setBackgroundColor(Color.parseColor(color))
    }
}
