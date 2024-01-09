package com.teamdontbe.feature.example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.teamdontbe.core_ui.view.ItemDiffCallback
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.feature.databinding.ItemExampleBinding
import com.teamdontbe.feature.example.viewholder.PagingDataViewHolder

class PagingAdapter(private val click: (UserEntity, Int) -> Unit = { _, _ -> }) :
    PagingDataAdapter<UserEntity, PagingDataViewHolder>(exampleDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PagingDataViewHolder {
        val binding =
            ItemExampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingDataViewHolder(binding, click)
    }

    override fun onBindViewHolder(
        holder: PagingDataViewHolder,
        position: Int,
    ) {
        getItem(position)?.let { holder.onBind(it) }
    }

    companion object {
        private val exampleDiffCallback =
            ItemDiffCallback<UserEntity>(
                onItemsTheSame = { old, new -> old.id == new.id },
                onContentsTheSame = { old, new -> old == new },
            )
    }
}
