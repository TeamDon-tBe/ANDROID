package com.teamdontbe.feature.mypage.comment

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.feature.databinding.ItemMyPageCommentBinding
import com.teamdontbe.feature.util.CalculateTime
import com.teamdontbe.feature.util.Transparent

class MyPageCommentViewHolder(
    private val binding: ItemMyPageCommentBinding,
    private val onClickKebabBtn: (MyPageCommentEntity, Int) -> Unit,
    private val onItemClicked: (MyPageCommentEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit = { _, _ -> },
    private val idFlag: Boolean,
    private val onClickTransparentBtn: (MyPageCommentEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    private var item: MyPageCommentEntity? = null

    init {
        binding.root.setOnClickListener {
            item?.let { onItemClicked(it) }
        }
        binding.btnCommentKebab.setOnClickListener {
            item?.let { onClickKebabBtn(it, bindingAdapterPosition) }
        }
    }

    fun onBind(data: MyPageCommentEntity) =
        with(binding) {
            if (!idFlag) {
                setVisibility()
            }

            tvCommentTransparency.text = "투명도 ${data.memberGhost}% · ${
                CalculateTime(root.context).getCalculateTime(data.time)
            }"
            feed = data
            item = data
            executePendingBindings()

            with(binding) {
                btnCommentHeart.isSelected = data.isLiked

                binding.btnCommentHeart.setOnClickListener {
                    onClickLikedBtn(data.commentId, btnCommentHeart.isSelected)
                    val likeNumber = tvCommentLikeNum.text.toString()
                    tvCommentLikeNum.text =
                        if (btnCommentHeart.isSelected) (likeNumber.toInt() - 1).toString() else (likeNumber.toInt() + 1).toString()
                    btnCommentHeart.isSelected = !btnCommentHeart.isSelected
                }
            }

            setTransparent(data)
        }

    private fun ItemMyPageCommentBinding.setVisibility() {
        ivCommentGhostFillGreen.visibility = View.VISIBLE
        dividerComment.visibility = View.VISIBLE
    }

    private fun setTransparent(data: MyPageCommentEntity) {
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
            binding.viewHomeTransparentBg.setBackgroundColor(Color.parseColor("#D9FCFCFD"))
        } else {
            val color = Transparent().calculateColorWithOpacity(data.memberGhost)
            binding.viewHomeTransparentBg.setBackgroundColor(Color.parseColor(color))
        }
    }
}
