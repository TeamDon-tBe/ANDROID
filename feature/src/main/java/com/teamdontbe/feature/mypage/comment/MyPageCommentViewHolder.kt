package com.teamdontbe.feature.mypage.comment

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.feature.databinding.ItemMyPageCommentBinding
import com.teamdontbe.feature.util.Transparent

class MyPageCommentViewHolder(
    private val binding: ItemMyPageCommentBinding,
    private val idFlag: Boolean,
    private val onItemClicked: (MyPageCommentEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickKebabBtn: (MyPageCommentEntity, Int) -> Unit,
    private val onClickTransparentBtn: (MyPageCommentEntity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var item: MyPageCommentEntity? = null

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

    fun onBind(data: MyPageCommentEntity) = with(binding) {
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

    private fun initLikedBtnCLickListener(data: MyPageCommentEntity) = with(binding) {
        btnCommentHeart.setOnClickListener {
            onClickLikedBtn(data.commentId, btnCommentHeart.isSelected)
            val likeNumber = tvCommentLikeNum.text.toString()
            tvCommentLikeNum.text =
                if (btnCommentHeart.isSelected) (likeNumber.toInt() - 1).toString() else (likeNumber.toInt() + 1).toString()
            btnCommentHeart.isSelected = !btnCommentHeart.isSelected
        }
    }
}
