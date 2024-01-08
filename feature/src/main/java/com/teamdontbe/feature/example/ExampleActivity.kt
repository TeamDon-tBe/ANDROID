package com.teamdontbe.feature.example

import androidx.activity.viewModels
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityExampleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExampleActivity : BindingActivity<ActivityExampleBinding>(R.layout.activity_example) {
    private val exampleAdapter = ExampleAdapter()

    private val viewModel by viewModels<ExampleViewModel>()

    override fun initView() {
        initAdapter(5)
    }

    private fun initAdapter(page: Int) {
//        binding.rvExample.adapter =
//            exampleAdapter(click = { _, _ ->
//            }).apply { pagingSubmitData(this, viewModel.getRecyclerviewTest(page), exampleAdapter) }
    }
}
