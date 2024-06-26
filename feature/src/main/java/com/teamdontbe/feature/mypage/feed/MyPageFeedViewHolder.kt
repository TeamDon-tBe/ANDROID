package com.teamdontbe.feature.mypage.feed

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.core_ui.view.setOnShortClickListener
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.util.Transparent

class MyPageFeedViewHolder(
    private val binding: ItemHomeFeedBinding,
    private val idFlag: Boolean,
    private val onItemClicked: (FeedEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit,
    private val onClickTransparentBtn: (FeedEntity) -> Unit,
    private val onClickFeedImage: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private var item: FeedEntity? = null

    init {
        binding.root.setOnClickListener {
            item?.let { onItemClicked(it) }
        }
        binding.btnHomeKebab.setOnClickListener {
            item?.let { onClickKebabBtn(it, bindingAdapterPosition) }
        }
        binding.ivHomeGhostFillGreen.setOnClickListener {
            item?.let { onClickTransparentBtn(it) }
        }
        binding.tvHomeFeedContent.setOnShortClickListener() {
            item?.run { onItemClicked(this) }
        }
        binding.ivHomeFeedImg.setOnClickListener {
            item?.contentImageUrl?.run { onClickFeedImage(this) }
        }
    }

    fun onBind(data: FeedEntity) = with(binding) {
        if (idFlag) {
            setVisibility()
        }
        feed = data
        item = data
        executePendingBindings()
        if (data.isGhost) setFeedTransparent(-85) else setFeedTransparent(data.memberGhost)
        btnHomeHeart.isSelected = data.isLiked

        initLikedBtnCLickListener(data)
    }

    private fun ItemHomeFeedBinding.setVisibility() {
        ivHomeGhostFillGreen.visibility = View.INVISIBLE
        ivHomeLinePale.visibility = View.INVISIBLE
    }

    private fun setFeedTransparent(memberGhostPercent: Int) {
        val color = Transparent().calculateColorWithOpacity(memberGhostPercent)
        binding.viewHomeTransparentBg.setBackgroundColor(Color.parseColor(color))
    }

    private fun initLikedBtnCLickListener(data: FeedEntity) {
        with(binding) {
            btnHomeHeart.setOnClickListener {
                data.contentId?.let { contentId ->
                    onClickLikedBtn(contentId, btnHomeHeart.isSelected)
                }
                val likeNumber = tvHomeHeartNum.text.toString()
                val likeNumberChanged =
                    if (btnHomeHeart.isSelected) (likeNumber.toInt() - 1) else (likeNumber.toInt() + 1)
                tvHomeHeartNum.text = likeNumberChanged.toString()
                data.isLiked = !btnHomeHeart.isSelected
                data.contentLikedNumber = likeNumberChanged
                btnHomeHeart.isSelected = !btnHomeHeart.isSelected
            }
        }
    }
}
