package com.teamdontbe.feature.mypage

import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.fragment.toast
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageBinding
import com.teamdontbe.feature.example.ExampleViewModel
import com.teamdontbe.feature.mypage.adapter.MyPageFeedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel by viewModels<ExampleViewModel>()

    override fun initView() {
        viewModel.getExampleRecyclerview(1)
        collectExample()
    }

    private fun collectExample() {
        viewModel.getExample.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Success -> {
                    binding.rvMyPageFeed.apply {
                        adapter = MyPageFeedAdapter(requireContext()).apply {
                            submitList(it.data)
                        }
                    }
                }

                is UiState.Empty -> {}
                is UiState.Failure -> toast("실패")
            }
        }.launchIn(lifecycleScope)
    }
}
