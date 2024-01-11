package com.teamdontbe.feature.mypage

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageBinding
import com.teamdontbe.feature.example.ExampleViewModel
import com.teamdontbe.feature.mypage.viewpager.MyPageVpAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel by viewModels<ExampleViewModel>()

    override fun initView() {
        viewModel.getRecyclerviewTest()
        binding.pbMyPageInput.setProgress(40)

        /*

                progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        // 프로그래스바의 진행률에 따라 TextView의 X 좌표를 조절
                        val newX = progress.toFloat() / progressBar.max * progressBar.width
                        moveTextView(newX)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        // 사용자가 터치를 시작할 때의 동작
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        // 사용자가 터치를 끝낼 때의 동작
                    }
                })
        */

//        collectExample()

        binding.vpMyPage.adapter = MyPageVpAdapter(this) // viewpager연동완료

        val tabTitleArray = arrayOf(
            POSTING,
            COMMENT,
        )

        TabLayoutMediator(binding.tabMyPage, binding.vpMyPage) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
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
    companion object {
        const val POSTING = "게시글"
        const val COMMENT = "답글"
    }
}
