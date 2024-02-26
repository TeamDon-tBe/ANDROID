package com.teamdontbe.feature.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.feature.databinding.ItemNotificationFeedBinding

class NotificationAdapter(
    private val click: (NotiEntity, Int) -> Unit = { _, _ -> },
) :
    ListAdapter<NotiEntity, NotificationViewHolder>(
            NotificationAdapterDiffCallback,
        ) {
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
        holder.bind(currentList[position])
    }

    companion object {
        val NotificationAdapterDiffCallback =
            ItemDiffCallback<NotiEntity>(
                onItemsTheSame = { old, new -> old.memberId == new.memberId },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
