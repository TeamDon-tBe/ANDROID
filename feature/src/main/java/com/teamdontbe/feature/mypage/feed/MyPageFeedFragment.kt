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
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.mypage.MyPageModel
import com.teamdontbe.feature.notification.NotificationFragment.Companion.KEY_NOTI_DATA
import com.teamdontbe.feature.util.FeedItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyPageFeedFragment :
    BindingFragment<FragmentMyPageFeedBinding>(R.layout.fragment_my_page_feed) {
    private val myPageFeedViewModel by viewModels<MyPageFeedViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()

    private lateinit var memberProfile: MyPageModel

    override fun initView() {
        // Arguments에서 memberProfile 값을 가져오기
        arguments?.let {
            memberProfile = it.getParcelable(ARG_MEMBER_PROFILE) ?: MyPageModel(
                -1,
                getString(R.string.my_page_nickname),
                false,
            )
        }
        initFeedObserve()
    }

    private fun initFeedObserve() {
        myPageFeedViewModel.getMyPageFeedList(memberProfile.id)
        myPageFeedViewModel.getMyPageFeedListState.flowWithLifecycle(lifecycle).onEach {
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

    private fun updateNoFeedUI() =
        with(binding) {
            rvMyPagePosting.visibility = View.GONE
            viewMyPageNoFeedNickname.clNoFeed.visibility = View.VISIBLE
            viewMyPageNoFeedNickname.tvNoFeedNickname.text =
                getString(R.string.my_page_no_feed_text, memberProfile.nickName)
        }

    private fun initFeedRecyclerView(feedEntity: List<FeedEntity>) {
        val myPageFeedAdapter = MyPageFeedAdapter(
            onClickKebabBtn = { feedEntity ->
                // Kebab 버튼 클릭 이벤트 처리
            },
            onItemClicked = { feedEntity ->
                // RecyclerView 항목 클릭 이벤트 처리
                navigateToHomeDetailFragment(feedEntity.contentId ?: -1)
            },
            onClickLikedBtn = { contentId, status ->
                if (status) {
                    homeViewModel.deleteFeedLiked(contentId)
                } else {
                    homeViewModel.postFeedLiked(
                        contentId,
                    )
                }
            },
            context = requireContext(),
            memberProfile.idFlag,
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

    //    action_fragment_home_to_fragment_my_page
    private fun navigateToHomeDetailFragment(id: Int) {
        findNavController().navigate(
            R.id.action_fragment_my_page_to_fragment_home_detail,
            bundleOf(KEY_NOTI_DATA to id),
        )
    }

    companion object {
        const val ARG_MEMBER_PROFILE = "arg_member_profile"

        fun newInstance(memberProfile: MyPageModel?): MyPageFeedFragment {
            return MyPageFeedFragment().apply {
                arguments = bundleOf(
                    ARG_MEMBER_PROFILE to memberProfile,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}
