package com.teamdontbe.feature.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding

class HomeViewHolder(
    private val binding: ItemHomeFeedBinding,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit = { _, _ -> },
    private val onClickToNavigateToHomeDetail: (FeedEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FeedEntity) {
        with(binding) {
//            tvHomeFeedTransparency.text = "투명도 ${data.memberGhost}% · ${
//                CalculateTime(binding.root.context).getCalculateTime(data.time)
//            }"

            feed = data
            executePendingBindings()
            btnHomeKebab.setOnClickListener {
                onClickKebabBtn(data, bindingAdapterPosition)
            }
            binding.root.setOnClickListener {
                onClickToNavigateToHomeDetail(data, bindingAdapterPosition)
            }
        }
    }
}
