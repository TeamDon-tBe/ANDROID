package com.teamdontbe.feature.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding

class HomeViewHolder(
    private val binding: ItemHomeFeedBinding,
    private val click: (FeedEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FeedEntity) {
        with(binding) {
            feed = data
            executePendingBindings()
            btnHomeKebab.setOnClickListener {
                click(data, adapterPosition)
            }
        }
    }
}
