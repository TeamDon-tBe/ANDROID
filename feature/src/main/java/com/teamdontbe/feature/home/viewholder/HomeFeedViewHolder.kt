package com.teamdontbe.feature.home.viewholder

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.util.Transparent

class HomeFeedViewHolder(
    private val binding: ItemHomeFeedBinding,
    private val userId: Int,
    private val onClickToNavigateToHomeDetail: (FeedEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickUserProfileBtn: (FeedEntity) -> Unit,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit,
    private val onClickTransparentBtn: (FeedEntity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FeedEntity) {
        with(binding) {
            feed = data
            executePendingBindings()


            if (data.isGhost) setFeedTransparent(-85) else setFeedTransparent(data.memberGhost)

            ivHomeGhostFillGreen.isVisible = data.memberId == userId
            ivHomeLinePale.isVisible = data.memberId == userId
            btnHomeHeart.isSelected = data.isLiked

            root.setOnClickListener {
                onClickToNavigateToHomeDetail(data)
            }

            initLikedBtnCLickListener(data)
            initProfileBtnClickListener(data)
            initKebabBtnClickListener(data)
            initGhostBtnClickListener(data)
        }
    }

    private fun initProfileBtnClickListener(data: FeedEntity) {
        binding.ivHomeProfile.setOnClickListener {
            onClickUserProfileBtn(data)
        }
    }

    private fun initLikedBtnCLickListener(data: FeedEntity) {
        with(binding) {
            btnHomeHeart.setOnClickListener {
                data.contentId?.let { contentId ->
                    onClickLikedBtn(contentId, btnHomeHeart.isSelected)
                }
                val likeNumber = tvHomeHeartNum.text.toString()
                tvHomeHeartNum.text =
                    if (btnHomeHeart.isSelected) (likeNumber.toInt() - 1).toString() else (likeNumber.toInt() + 1).toString()
                btnHomeHeart.isSelected = !btnHomeHeart.isSelected
            }
        }
    }

    private fun initKebabBtnClickListener(data: FeedEntity) {
        binding.btnHomeKebab.setOnClickListener {
            onClickKebabBtn(data, bindingAdapterPosition)
        }
    }

    private fun initGhostBtnClickListener(data: FeedEntity) {
        binding.ivHomeGhostFillGreen.setOnClickListener {
            onClickTransparentBtn(data)
        }
    }

    private fun setFeedTransparent(memberGhostPercent: Int) {
        val color = Transparent().calculateColorWithOpacity(memberGhostPercent)
        binding.viewHomeTransparentBg.setBackgroundColor(Color.parseColor(color))
    }
}
