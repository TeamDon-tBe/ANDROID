package com.teamdontbe.feature.mypage.feed

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.util.CalculateTime

class MyPageFeedViewHolder(
    private val binding: ItemHomeFeedBinding,
    private val onClickKebabBtn: (FeedEntity) -> Unit,
    private val onItemClicked: (FeedEntity) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    private var item: FeedEntity? = null

    init {
        binding.root.setOnClickListener {
            item?.let { onItemClicked(it) }
        }
        binding.btnHomeKebab.setOnClickListener {
            item?.let { onClickKebabBtn(it) }
        }
    }

    fun onBind(data: FeedEntity) = with(binding) {
        setVisibility()
        tvHomeFeedTransparency.text = "투명도 + ${data.memberGhost}% · ${
            CalculateTime(root.context).getCalculateTime(data.time)
        }"
        feed = data
        item = data
        executePendingBindings()
    }

    private fun ItemHomeFeedBinding.setVisibility() {
        ivHomeGhostFillGreen.visibility = View.INVISIBLE
        ivHomeLinePale.visibility = View.INVISIBLE
    }
}
