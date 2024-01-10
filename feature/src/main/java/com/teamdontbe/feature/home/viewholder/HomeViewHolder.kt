package com.teamdontbe.feature.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import timber.log.Timber

class HomeViewHolder(
    private val binding: ItemHomeFeedBinding,
    private val click: (FeedEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: FeedEntity) {
        with(binding) {
            Timber.d(data.toString())
            binding.tvHomeHeartNum.text = data.contentLikedNumber.toString()
            binding.tvHomeCommentNum.text = data.commentNumber.toString()
            feed = data
            executePendingBindings()
            binding.btnHomeKebab.setOnClickListener {
                click(data, adapterPosition)
            }
        }
    }
}
