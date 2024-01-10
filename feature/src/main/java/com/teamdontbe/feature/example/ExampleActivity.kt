package com.teamdontbe.feature.example

import androidx.activity.viewModels
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityExampleBinding
import com.teamdontbe.feature.util.pagingSubmitData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExampleActivity : BindingActivity<ActivityExampleBinding>(R.layout.activity_example) {
    private val exampleAdapter =
        PagingAdapter(click = { data, position ->
        })

    private val viewModel by viewModels<ExampleViewModel>()

    override fun initView() {
        initAdapter()
    }

    private fun initAdapter() {
        binding.rvExample.adapter =
            exampleAdapter.apply {
                pagingSubmitData(
                    this@ExampleActivity,
                    viewModel.getRecyclerviewTest(),
                    exampleAdapter,
                )
            }
    }
}
