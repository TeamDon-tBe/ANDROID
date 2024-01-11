package com.teamdontbe.feature.mypage

import androidx.fragment.app.viewModels
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageBinding
import com.teamdontbe.feature.example.ExampleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel by viewModels<ExampleViewModel>()

    override fun initView() {
        viewModel.getRecyclerviewTest()
//        collectExample()
    }

    /*
        private fun collectExample() {
            viewModel.getExample.flowWithLifecycle(lifecycle).onEach {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        binding.rvMyPageFeed.apply {
                            adapter = MyPageFeedAdapter(requireContext()).apply {
                                submitList(it.data)
                            }
                            addItemDecoration(MyPageFeedItemDecorator(requireContext()))
                        }
                    }

                    is UiState.Empty -> {}
                    is UiState.Failure -> toast("실패")
                }
            }.launchIn(lifecycleScope)
        }
    */
}
