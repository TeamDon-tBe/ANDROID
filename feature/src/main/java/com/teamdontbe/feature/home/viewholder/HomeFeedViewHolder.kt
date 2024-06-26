package com.teamdontbe.feature.home.viewholder

import android.content.Context
import android.graphics.Color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.core_ui.view.setOnDuplicateBlockClick
import com.teamdontbe.core_ui.view.setOnShortClickListener
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.util.CalculateTime
import com.teamdontbe.feature.util.Transparent

class HomeFeedViewHolder(
    private val context: Context,
    private val binding: ItemHomeFeedBinding,
    private val userId: Int,
    private val onClickToNavigateToHomeDetail: (FeedEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickUserProfileBtn: (Int) -> Unit,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit,
    private val onClickTransparentBtn: (FeedEntity) -> Unit,
    private val onClickFeedImage: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FeedEntity) {
        with(binding) {
            feed = data
            executePendingBindings()

            if (data.isGhost) {
                setFeedTransparent(-85)
                binding.tvHomeFeedTransparency.text = context.getString(
                    R.string.tv_transparency_complete,
                    data.memberGhost,
                    CalculateTime(context).getCalculateTime(data.time)
                )
            } else setFeedTransparent(data.memberGhost)

            ivHomeGhostFillGreen.isVisible = data.memberId !== userId
            ivHomeLinePale.isVisible = data.memberId !== userId
            btnHomeHeart.isSelected = data.isLiked

            root.setOnClickListener {
                onClickToNavigateToHomeDetail(data)
            }
            binding.tvHomeFeedContent.setOnShortClickListener {
                onClickToNavigateToHomeDetail(data)
            }
            binding.ivHomeFeedImg.setOnClickListener {
                data.contentImageUrl?.run { onClickFeedImage(this) }
            }
            initLikedBtnCLickListener(data)
            initProfileBtnClickListener(data)
            initKebabBtnClickListener(data)
            initGhostBtnClickListener(data)
        }
    }

    private fun initProfileBtnClickListener(data: FeedEntity) {
        binding.ivHomeProfile.setOnClickListener {
            if (data.isDeleted == false) onClickUserProfileBtn(data.memberId)
        }
        binding.tvHomeFeedUserName.setOnClickListener {
            if (data.isDeleted == false) onClickUserProfileBtn(data.memberId)
        }
    }

    private fun initLikedBtnCLickListener(data: FeedEntity) {
        with(binding) {
            btnHomeHeart.setOnDuplicateBlockClick {
                data.contentId?.let { contentId ->
                    onClickLikedBtn(contentId, btnHomeHeart.isSelected)
                }
                val likeNumber = tvHomeHeartNum.text.toString()
                val likeNumberChanged = if (btnHomeHeart.isSelected) (likeNumber.toInt() - 1) else (likeNumber.toInt() + 1)
                tvHomeHeartNum.text = likeNumberChanged.toString()
                data.isLiked = !btnHomeHeart.isSelected
                data.contentLikedNumber = likeNumberChanged
                btnHomeHeart.isSelected = !btnHomeHeart.isSelected
            }
        }
    }

    private fun initKebabBtnClickListener(data: FeedEntity) =
        binding.btnHomeKebab.setOnClickListener {
            onClickKebabBtn(data, bindingAdapterPosition)
        }

    private fun initGhostBtnClickListener(data: FeedEntity) =
        binding.ivHomeGhostFillGreen.setOnClickListener {
            onClickTransparentBtn(data)
        }

    private fun setFeedTransparent(memberGhostPercent: Int) {
        val color = Transparent().calculateColorWithOpacity(memberGhostPercent)
        binding.viewHomeTransparentBg.setBackgroundColor(Color.parseColor(color))
    }
}
