package com.teamdontbe.feature.home

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomeBinding
import com.teamdontbe.feature.mypage.adapter.MyPageFeedItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun initView() {
        initObserve()
    }

    private fun initObserve() {
        homeViewModel.getFeedList.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    initHomeAdapter(it.data)
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initHomeAdapter(feedData: List<FeedEntity>) {
        binding.rvHome.adapter =
            HomeAdapter(onClickKebabBtn = { feedData, positoin ->
                initBottomSheet()
            }, onClickToNavigateToHomeDetail = { feedData, position ->
                navigateToHomeDetailFragment(
                    Feed(
                        feedData.memberId,
                        feedData.memberNickname,
                        feedData.memberNickname,
                        feedData.isLiked,
                        feedData.isGhost,
                        feedData.memberGhost,
                        feedData.contentLikedNumber,
                        feedData.commentNumber,
                        feedData.contentText,
                        feedData.time,
                    ),
                )
            }).apply {
                submitList(feedData)
            }

        binding.rvHome.addItemDecoration(MyPageFeedItemDecorator(requireContext()))
    }

    private fun initBottomSheet() {
        HomeBottomSheet().show(parentFragmentManager, HOME_BOTTOM_SHEET)
    }

    private fun navigateToHomeDetailFragment(feedData: Feed) {
        findNavController().navigate(
            R.id.action_home_to_home_detail,
            bundleOf(KEY_FEED_DATA to feedData),
        )
    }

    companion object {
        const val HOME_BOTTOM_SHEET = "home_bottom_sheet"
        const val KEY_FEED_DATA = "key_feed_data"
    }
}
