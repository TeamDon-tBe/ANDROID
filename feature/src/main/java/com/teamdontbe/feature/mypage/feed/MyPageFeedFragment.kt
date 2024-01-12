package com.teamdontbe.feature.mypage.feed

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageFeedBinding
import com.teamdontbe.feature.home.Feed
import com.teamdontbe.feature.home.HomeFragment
import com.teamdontbe.feature.mypage.MyPageViewModel
import com.teamdontbe.feature.mypage.adapter.MyPageFeedItemDecorator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFeedFragment :
    BindingFragment<FragmentMyPageFeedBinding>(R.layout.fragment_my_page_feed) {
    private val mockDataViewModel by viewModels<MyPageViewModel>()

    override fun initView() {
        initFeedRecyclerView()
        updateNoFeedUI()
    }

    private fun updateNoFeedUI() = with(binding) {
        rvMyPagePosting.visibility = View.GONE
        viewMyPageNoFeedNickname.clNoFeed.visibility = View.VISIBLE
    }

    private fun initFeedRecyclerView() {
        val myPageFeedAdapter = MyPageFeedAdapter(
            onClickKebabBtn = { feedEntity ->
                // Kebab 버튼 클릭 이벤트 처리
                // feedEntity를 사용하여 필요한 작업 수행
                // 예: viewModel.getRecyclerviewTest()
            },
            onItemClicked = { feedData ->
                // RecyclerView 항목 클릭 이벤트 처리
                // feedEntity를 사용하여 필요한 작업 수행
                navigateToHomeDetailFragment(
//                    feedEntity.memberId,
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
            },
            context = requireContext(),
        ).apply {
            submitList(mockDataViewModel.mockDataList.toMutableList())
        }

        setUpFeedAdapter(myPageFeedAdapter)
    }

    private fun setUpFeedAdapter(myPageFeedAdapter: MyPageFeedAdapter) {
        binding.rvMyPagePosting.apply {
            adapter = myPageFeedAdapter
            addItemDecoration(MyPageFeedItemDecorator(requireContext()))
        }
    }

    private fun navigateToHomeDetailFragment(id: Feed) {
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
