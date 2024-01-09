package com.teamdontbe.feature.example

import androidx.activity.viewModels
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityExampleBinding
import com.teamdontbe.feature.util.pagingSubmitData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExampleActivity : BindingActivity<ActivityExampleBinding>(R.layout.activity_example) {
    private val exampleAdapter = PagingAdapter()

    private val viewModel by viewModels<ExampleViewModel>()

    override fun initView() {
        initAdapter(1)
    }

    private fun initAdapter(page: Int) {
        binding.rvExample.adapter =
            exampleAdapter.apply { pagingSubmitData(this@ExampleActivity, viewModel.getRecyclerviewTest(page), exampleAdapter) }
    }
}
