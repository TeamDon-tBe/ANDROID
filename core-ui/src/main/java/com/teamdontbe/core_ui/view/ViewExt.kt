package com.teamdontbe.core_ui.view

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.flow.Flow

class ItemDiffCallback<T : Any>(
    val onItemsTheSame: (T, T) -> Boolean,
    val onContentsTheSame: (T, T) -> Boolean
) : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(
        oldItem: T, newItem: T
    ): Boolean = onItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(
        oldItem: T, newItem: T
    ): Boolean = onContentsTheSame(oldItem, newItem)
}