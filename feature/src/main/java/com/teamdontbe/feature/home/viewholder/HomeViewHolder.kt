package com.teamdontbe.feature.home.viewholder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.util.CalculateTime
import com.teamdontbe.feature.util.Transparent
import timber.log.Timber

class HomeViewHolder(
    private val binding: ItemHomeFeedBinding,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
    private val onClickToNavigateToHomeDetail: (FeedEntity, Int) -> Unit = { _, _ -> },
    private val onClickLikedBtn: (Int, Boolean) -> Unit = { _, _ -> },
    private val onClickUserProfileBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
    private val onClickTransparentBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FeedEntity) {
        with(binding) {
            if (data.time.isNotEmpty()) {
                tvHomeFeedTransparency.text = "투명도 ${data.memberGhost}% · ${
                    CalculateTime(binding.root.context).getCalculateTime(data.time)
                }"
            }
            btnHomeHeart.isSelected = data.isLiked
            feed = data
            executePendingBindings()
            btnHomeKebab.setOnClickListener {
                onClickKebabBtn(data, bindingAdapterPosition)
            }
            root.setOnClickListener {
                onClickToNavigateToHomeDetail(data, bindingAdapterPosition)
            }
            btnHomeHeart.setOnClickListener {
                data.contentId?.let { contentId ->
                    onClickLikedBtn(contentId, btnHomeHeart.isSelected)
                }
                val likeNumber = tvHomeHeartNum.text.toString()
                tvHomeHeartNum.text =
                    if (btnHomeHeart.isSelected) (likeNumber.toInt() - 1).toString() else (likeNumber.toInt() + 1).toString()
                btnHomeHeart.isSelected = !btnHomeHeart.isSelected
            }
            ivHomeProfile.setOnClickListener {
                onClickUserProfileBtn(data, bindingAdapterPosition)
            }
            ivHomeGhostFillGreen.setOnClickListener {
                onClickTransparentBtn(data, position)
            }

            if (data.isGhost) {
                binding.viewHomeTransparentBg.setBackgroundColor(Color.parseColor("#D9FCFCFD"))
            } else {
                val color = Transparent().calculateColorWithOpacity(data.memberGhost)
                Timber.tag("color").d(color)
                binding.viewHomeTransparentBg.setBackgroundColor(Color.parseColor(color))
            }
        }
    }
}
