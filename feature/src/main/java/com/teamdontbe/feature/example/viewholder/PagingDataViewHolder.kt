package com.teamdontbe.feature.example.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.feature.databinding.ItemExampleBinding

class PagingDataViewHolder(private val binding: ItemExampleBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: UserEntity) {
        binding.example = data
        binding.executePendingBindings()
    }
}
