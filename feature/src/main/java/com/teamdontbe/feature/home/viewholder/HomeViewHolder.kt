package com.teamdontbe.feature.home.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R
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
    private val userId: Int,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FeedEntity) {
        with(binding) {
            ivHomeProfile.load(R.drawable.ic_sign_up_profile_person)
            if (data.memberId == userId) {
                ivHomeGhostFillGreen.visibility = View.INVISIBLE
                ivHomeLinePale.visibility = View.INVISIBLE
            } else {
                ivHomeGhostFillGreen.visibility = View.VISIBLE
                ivHomeLinePale.visibility = View.VISIBLE
            }

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

            setTransparent(data)
        }
    }

    private fun setTransparent(data: FeedEntity) {
        binding.ivHomeGhostFillGreen.setOnClickListener {
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
            Timber.tag("color").d(color)
            binding.viewHomeTransparentBg.setBackgroundColor(Color.parseColor(color))
        }
    }
}
