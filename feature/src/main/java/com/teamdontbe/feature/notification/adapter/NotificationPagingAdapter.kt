package com.teamdontbe.feature.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.feature.databinding.ItemNotificationFeedBinding
import com.teamdontbe.feature.notification.adapter.NotificationAdapter.Companion.NotificationAdapterDiffCallback

class NotificationPagingAdapter(
    private val click: (NotiEntity, Int) -> Unit = { _, _ -> },
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
}
