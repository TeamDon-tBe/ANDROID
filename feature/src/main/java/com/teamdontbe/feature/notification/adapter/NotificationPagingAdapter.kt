package com.teamdontbe.feature.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.feature.databinding.ItemNotificationFeedBinding

class NotificationPagingAdapter(
    private val click: (NotiEntity, Int) -> Unit,
) :
    PagingDataAdapter<NotiEntity, NotificationViewHolder>(NotificationAdapterDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NotificationViewHolder {
        val binding =
            ItemNotificationFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding, click)
    }

    override fun onBindViewHolder(
        holder: NotificationViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val NotificationAdapterDiffCallback =
            ItemDiffCallback<NotiEntity>(
                onItemsTheSame = { old, new -> old.memberId == new.memberId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
