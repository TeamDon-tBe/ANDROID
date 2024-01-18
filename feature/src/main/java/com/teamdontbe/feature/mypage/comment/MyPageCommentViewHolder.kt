package com.teamdontbe.feature.mypage.comment

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.feature.databinding.ItemMyPageCommentBinding
import com.teamdontbe.feature.util.CalculateTime

class MyPageCommentViewHolder(
    private val binding: ItemMyPageCommentBinding,
    private val onClickKebabBtn: (MyPageCommentEntity) -> Unit,
    private val onItemClicked: (MyPageCommentEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit = { _, _ -> },
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

    fun onBind(data: MyPageCommentEntity) =
        with(binding) {
            tvCommentTransparency.text = "투명도 ${data.memberGhost}% · ${
                CalculateTime(root.context).getCalculateTime(data.time)
            }"
            btnCommentHeart.isSelected = data.isLiked

            feed = data
            item = data
            executePendingBindings()

            binding.btnCommentHeart.setOnClickListener {
                onClickLikedBtn(data.commentId, btnCommentHeart.isSelected)
                val likeNumber = tvCommentLikeNum.text.toString()
                tvCommentLikeNum.text =
                    if (btnCommentHeart.isSelected) (likeNumber.toInt() - 1).toString() else (likeNumber.toInt() + 1).toString()
                btnCommentHeart.isSelected = !btnCommentHeart.isSelected
            }
        }
}
