package com.teamdontbe.feature.mypage.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding

class MyPageFeedAdapter(
    private val onClickKebabBtn: (FeedEntity) -> Unit,
    private val onItemClicked: (FeedEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    context: Context,
    private val idFlag: Boolean,
) :
    ListAdapter<FeedEntity, MyPageFeedViewHolder>(ExampleDiffCallback) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageFeedViewHolder {
        val binding = ItemHomeFeedBinding.inflate(inflater, parent, false)
        return MyPageFeedViewHolder(
            binding,
            onClickKebabBtn,
            onItemClicked,
            onClickLikedBtn,
            idFlag,
        )
    }

    override fun onBindViewHolder(holder: MyPageFeedViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        private val ExampleDiffCallback =
            ItemDiffCallback<FeedEntity>(
                onItemsTheSame = { old, new -> old.memberId == new.memberId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
