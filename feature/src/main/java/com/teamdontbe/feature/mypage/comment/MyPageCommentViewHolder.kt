package com.teamdontbe.feature.mypage.comment

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.feature.databinding.ItemMyPageCommentBinding
import com.teamdontbe.feature.util.Transparent

class MyPageCommentViewHolder(
    private val binding: ItemMyPageCommentBinding,
    private val idFlag: Boolean,
    private val onItemClicked: (CommentEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickKebabBtn: (CommentEntity, Int) -> Unit,
    private val onClickTransparentBtn: (CommentEntity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var item: CommentEntity? = null

    init {
        binding.root.setOnClickListener {
            item?.let { onItemClicked(it) }
        }
        binding.btnCommentKebab.setOnClickListener {
            item?.let { onClickKebabBtn(it, bindingAdapterPosition) }
        }
        binding.ivCommentGhostFillGreen.setOnClickListener {
            item?.let { onClickTransparentBtn(it) }
        }
    }

    fun onBind(data: CommentEntity) = with(binding) {
        if (!idFlag) {
            setVisibility()
        }
        feed = data
        item = data
        executePendingBindings()
        if (data.isGhost) setCommentTransparent(-85) else setCommentTransparent(data.memberGhost)
        btnCommentHeart.isSelected = data.isLiked

        initLikedBtnCLickListener(data)
    }

    private fun ItemMyPageCommentBinding.setVisibility() {
        ivCommentGhostFillGreen.visibility = View.VISIBLE
        dividerComment.visibility = View.VISIBLE
    }

    private fun setCommentTransparent(memberGhostPercent: Int) {
        val color = Transparent().calculateColorWithOpacity(memberGhostPercent)
        binding.viewHomeTransparentBg.setBackgroundColor(Color.parseColor(color))
    }

    private fun initLikedBtnCLickListener(data: CommentEntity) = with(binding) {
        btnCommentHeart.setOnClickListener {
            onClickLikedBtn(data.commentId, btnCommentHeart.isSelected)
            val likeNumber = tvCommentLikeNum.text.toString()
            val likeNumberChanged =
                if (btnCommentHeart.isSelected) (likeNumber.toInt() - 1) else (likeNumber.toInt() + 1)
            tvCommentLikeNum.text = likeNumberChanged.toString()
            data.isLiked = !btnCommentHeart.isSelected
            data.commentLikedNumber = likeNumberChanged
            btnCommentHeart.isSelected = !btnCommentHeart.isSelected
        }
    }
}
