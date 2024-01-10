package com.teamdontbe.feature.example

import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.domain.entity.UserEntity
import com.teamdontbe.feature.databinding.ItemExampleBinding

class ExampleViewHolder(
    private val binding: ItemExampleBinding,
    private val click: (UserEntity, Int) -> Unit = { _, _ -> },
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: UserEntity) {
        with(binding) {
            example = data
            executePendingBindings()
            binding.root.setOnClickListener {
                click(data, bindingAdapterPosition)
            }
        }
    }
}
