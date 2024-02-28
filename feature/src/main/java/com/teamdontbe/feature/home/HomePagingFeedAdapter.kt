package com.teamdontbe.feature.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding
import com.teamdontbe.feature.home.HomeFeedAdapter.Companion.HomeAdapterDiffCallback
import com.teamdontbe.feature.home.viewholder.HomeFeedViewHolder

class HomePagingFeedAdapter(
    private val context : Context,
    private val userId: Int,
    private val onClickToNavigateToHomeDetail: (FeedEntity) -> Unit,
    private val onClickLikedBtn: (Int, Boolean) -> Unit,
    private val onClickUserProfileBtn: (Int) -> Unit,
    private val onClickKebabBtn: (FeedEntity, Int) -> Unit,
    private val onClickTransparentBtn: (FeedEntity) -> Unit,
) :
    PagingDataAdapter<FeedEntity, HomeFeedViewHolder>(HomeAdapterDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HomeFeedViewHolder {
        val binding =
            ItemHomeFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeFeedViewHolder(
            context,
            binding,
            userId,
            onClickToNavigateToHomeDetail,
            onClickLikedBtn,
            onClickUserProfileBtn,
            onClickKebabBtn,
            onClickTransparentBtn
        )

    }

    override fun onBindViewHolder(
        holder: HomeFeedViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun deleteItem(position: Int) {
        notifyItemRemoved(position)
    }
}