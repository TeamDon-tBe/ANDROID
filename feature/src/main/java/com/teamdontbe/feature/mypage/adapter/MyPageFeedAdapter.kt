package com.teamdontbe.feature.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.feature.databinding.ItemHomeFeedBinding

class MyPageFeedAdapter(context: Context) :
    ListAdapter<UserEntity, MyPageFeedViewHolder>(ExampleDiffCallback) {
    private val inflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageFeedViewHolder {
        val binding = ItemHomeFeedBinding.inflate(inflater, parent, false)
        return MyPageFeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageFeedViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    companion object {
        private val ExampleDiffCallback =
            ItemDiffCallback<UserEntity>(
                onItemsTheSame = { old, new -> old.id == new.id },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
