package com.teamdontbe.feature.mypage.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding

class MyPageFeedAdapter(
    context: Context,
    private val idFlag: Boolean,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit,
    private val onItemClicked: (FeedEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickTransparentBtn: (FeedEntity) -> Unit,
    private val onClickFeedImage: (String) -> Unit,
) : PagingDataAdapter<FeedEntity, MyPageFeedViewHolder>(myPageFeedItemDiffCallback) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyPageFeedViewHolder {
        val binding = ItemHomeFeedBinding.inflate(inflater, parent, false)
        return MyPageFeedViewHolder(
            binding = binding,
            idFlag = idFlag,
            onClickKebabBtn = onClickKebabBtn,
            onItemClicked = onItemClicked,
            onClickLikedBtn = onClickLikedBtn,
            onClickTransparentBtn = onClickTransparentBtn,
            onClickFeedImage = onClickFeedImage,
        )
    }

    override fun onBindViewHolder(
        holder: MyPageFeedViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { holder.onBind(it) }
    }

    fun deleteItem(position: Int) {
        notifyItemRemoved(position)
    }

    companion object {
        private val myPageFeedItemDiffCallback =
            ItemDiffCallback<FeedEntity>(
                onItemsTheSame = { old, new -> old.contentId == new.contentId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
