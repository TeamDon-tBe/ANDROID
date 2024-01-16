package com.teamdontbe.feature.mypage.feed

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageFeedBinding
import com.teamdontbe.feature.home.HomeFragment
import com.teamdontbe.feature.util.FeedItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyPageFeedFragment(data: Int) :
    BindingFragment<FragmentMyPageFeedBinding>(R.layout.fragment_my_page_feed) {
    private val mockDataViewModel by viewModels<MyPageFeedViewModel>()
    private val testId = data ?: -1

    override fun initView() {
        initFeedObserve(testId)
    }

    private fun initFeedObserve(testId: Int) {
        mockDataViewModel.getMyPageUserProfileInfo(testId)
        mockDataViewModel.getMyPageFeedListState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> handleSuccessState(it.data)
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun handleSuccessState(feedList: List<FeedEntity>) {
        if (feedList.isEmpty()) {
            updateNoFeedUI()
        } else {
            initFeedRecyclerView(feedList)
        }
    }

    private fun updateNoFeedUI() = with(binding) {
        rvMyPagePosting.visibility = View.GONE
        viewMyPageNoFeedNickname.clNoFeed.visibility = View.VISIBLE
    }

    private fun initFeedRecyclerView(feedEntity: List<FeedEntity>) {
        val myPageFeedAdapter = MyPageFeedAdapter(
            onClickKebabBtn = { feedEntity ->
                // Kebab 버튼 클릭 이벤트 처리
                // feedEntity를 사용하여 필요한 작업 수행
                // 예: viewModel.getRecyclerviewTest()
            },
            onItemClicked = { feedEntity ->
                // RecyclerView 항목 클릭 이벤트 처리
                navigateToHomeDetailFragment(
                    feedEntity.memberId,
                )
            },
            context = requireContext(),
        ).apply {
            submitList(feedEntity)
        }

        setUpFeedAdapter(myPageFeedAdapter)
    }

    private fun setUpFeedAdapter(myPageFeedAdapter: MyPageFeedAdapter) {
        binding.rvMyPagePosting.apply {
            adapter = myPageFeedAdapter
            addItemDecoration(FeedItemDecorator(requireContext()))
        }
    }

    private fun navigateToHomeDetailFragment(id: Int) {
        findNavController().navigate(
            R.id.action_fragment_my_page_to_fragment_home_detail,
            bundleOf(HomeFragment.KEY_FEED_DATA to id),
        )
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}
