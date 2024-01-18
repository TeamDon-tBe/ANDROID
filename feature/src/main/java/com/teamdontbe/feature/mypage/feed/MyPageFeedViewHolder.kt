package com.teamdontbe.feature.mypage.feed

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.util.CalculateTime
import com.teamdontbe.feature.util.Transparent
import timber.log.Timber

class MyPageFeedViewHolder(
    private val binding: ItemHomeFeedBinding,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit,
    private val onItemClicked: (FeedEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val idFlag: Boolean,
    private val onClickTransparentBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    private var item: FeedEntity? = null

    init {
        binding.root.setOnClickListener {
            item?.let { onItemClicked(it) }
        }
        binding.btnHomeKebab.setOnClickListener {
            item?.let { onClickKebabBtn(it, bindingAdapterPosition) }
        }
    }

    fun onBind(data: FeedEntity) =
        with(binding) {
            if (idFlag) {
                setVisibility()
            }
            tvHomeFeedTransparency.text = "투명도 ${data.memberGhost}% · ${
                CalculateTime(root.context).getCalculateTime(data.time)
            }"
            btnHomeHeart.isSelected = data.isLiked
            feed = data
            item = data
            executePendingBindings()
            setupLikeButton(data)
            setTransparent(data)
        }

    private fun ItemHomeFeedBinding.setVisibility() {
        ivHomeGhostFillGreen.visibility = View.INVISIBLE
        ivHomeLinePale.visibility = View.INVISIBLE
    }

    private fun setupLikeButton(data: FeedEntity) =
        with(binding) {
            btnHomeHeart.setOnClickListener {
                data.contentId?.let { contentId ->
                    onClickLikedBtn(contentId, btnHomeHeart.isSelected)
                }
                val likeNumber = tvHomeHeartNum.text.toString().toInt()
                tvHomeHeartNum.text =
                    (if (btnHomeHeart.isSelected) likeNumber - 1 else likeNumber + 1).toString()
                btnHomeHeart.isSelected = !btnHomeHeart.isSelected
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
                binding.tvHomeFeedTransparency.text = (data.memberGhost - 1).toString()
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
