package com.teamdontbe.feature.example.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.feature.databinding.ItemExampleBinding

class PagingDataViewHolder(
    private val binding: ItemExampleBinding,
    private val click: (UserEntity, Int) -> Unit = { _, _ -> },
) :
    RecyclerView.ViewHolder(binding.root) {
    fun onBind(data: UserEntity) {
        binding.example = data
        binding.executePendingBindings()
        binding.root.setOnClickListener {
            click(data, bindingAdapterPosition)
        }
    }
}
