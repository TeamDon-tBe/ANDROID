package com.teamdontbe.feature.mypage.feed

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageFeedBinding
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.home.HomeFragment
import com.teamdontbe.feature.homedetail.HomeDetailFragment.Companion.ALARM_TRIGGER_TYPE_CONTENT
import com.teamdontbe.feature.mypage.MyPageFragment.Companion.MY_PAGE_ANOTHER_BOTTOM_SHEET
import com.teamdontbe.feature.mypage.MyPageModel
import com.teamdontbe.feature.mypage.MyPageViewModel
import com.teamdontbe.feature.mypage.bottomsheet.MyPageAnotherUserBottomSheet
import com.teamdontbe.feature.mypage.bottomsheet.MyPageTransparentDialogFragment
import com.teamdontbe.feature.notification.NotificationFragment.Companion.KEY_NOTI_DATA
import com.teamdontbe.feature.posting.PostingFragment
import com.teamdontbe.feature.snackbar.TransparentIsGhostSnackBar
import com.teamdontbe.feature.util.FeedItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyPageFeedFragment :
    BindingFragment<FragmentMyPageFeedBinding>(R.layout.fragment_my_page_feed) {
    private val myPageFeedViewModel by activityViewModels<MyPageViewModel>()

    private lateinit var memberProfile: MyPageModel
    private lateinit var myPageFeedAdapter: MyPageFeedAdapter
    private var deleteFeedPosition: Int = -1

    override fun initView() {
        // Arguments에서 memberProfile 값을 가져오기
        initMemberProfile()
        initFeedObserve()
        initDeleteObserve()
        initTransparentObserve()
    }

    private fun initMemberProfile() {
        arguments?.let {
            memberProfile = it.getParcelable(ARG_MEMBER_PROFILE) ?: MyPageModel(
                -1,
                getString(R.string.my_page_nickname),
                false,
            )
        }
    }

    private fun initFeedObserve() {
        myPageFeedViewModel.getMyPageFeedList(memberProfile.id)
        myPageFeedViewModel.getMyPageFeedListState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    initDeleteObserve()
                    handleSuccessState(it.data)
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initDeleteObserve() {
        myPageFeedViewModel.deleteFeed.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit

                is UiState.Success -> {
                    if (deleteFeedPosition != -1) {
                        myPageFeedAdapter.deleteItem(deleteFeedPosition)
                        deleteFeedPosition = -1
                    }
                    val dialog = DeleteCompleteDialogFragment()
                    dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initTransparentObserve() {
        myPageFeedViewModel.postTransparent.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> myPageFeedViewModel.getMyPageFeedList(memberProfile.id)
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
            viewMyPageNoFeedNickname.btnNoFeedPosting.setOnClickListener {
                navigateToPostingFragment(memberProfile.id)
            }
        }

    private fun initFeedRecyclerView(feedEntity: List<FeedEntity>) {
        myPageFeedAdapter =
            MyPageFeedAdapter(
                onClickKebabBtn = { feedEntity, position ->
                    // Kebab 버튼 클릭 이벤트 처리
                    feedEntity.contentId?.let {
                        initBottomSheet(
                            feedEntity.memberId == myPageFeedViewModel.getMemberId(),
                            contentId = it,
                            commentId = -1,
                            whereFrom = FROM_FEED,
                        )
                        deleteFeedPosition = position
                    }
                },
                onItemClicked = { feedEntity ->
                    // RecyclerView 항목 클릭 이벤트 처리
                    navigateToHomeDetailFragment(feedEntity.contentId ?: -1)
                },
                onClickLikedBtn = { contentId, status ->
                    if (status) {
                        myPageFeedViewModel.deleteFeedLiked(contentId)
                    } else {
                        myPageFeedViewModel.postFeedLiked(
                            contentId,
                        )
                    }
                },
                context = requireContext(),
                memberProfile.idFlag,
                onClickTransparentBtn = { data, position ->
                    if (position == -2) {
                        TransparentIsGhostSnackBar.make(binding.root).show()
                    } else {
                        initTransparentDialog(data.memberId, data.contentId ?: -1)
                    }
                },
            ).apply {
                submitList(feedEntity)
            }

        setUpFeedAdapter(myPageFeedAdapter)
    }

    private fun initTransparentDialog(
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) {
        val dialog = MyPageTransparentDialogFragment(ALARM_TRIGGER_TYPE_CONTENT, targetMemberId, alarmTriggerId)
        dialog.show(childFragmentManager, HomeFragment.HOME_TRANSPARENT_DIALOG)
    }

    private fun initBottomSheet(
        isMember: Boolean,
        contentId: Int,
        commentId: Int,
        whereFrom: String,
    ) {
        MyPageAnotherUserBottomSheet(isMember, contentId, commentId, whereFrom).show(
            parentFragmentManager,
            MY_PAGE_ANOTHER_BOTTOM_SHEET,
        )
    }

    private fun setUpFeedAdapter(myPageFeedAdapter: MyPageFeedAdapter) {
        binding.rvMyPagePosting.adapter = myPageFeedAdapter
        if (binding.rvMyPagePosting.itemDecorationCount == 0) {
            binding.rvMyPagePosting.addItemDecoration(
                FeedItemDecorator(requireContext()),
            )
        }
    }

    private fun navigateToHomeDetailFragment(id: Int) {
        findNavController().navigate(
            R.id.action_fragment_my_page_to_fragment_home_detail,
            bundleOf(KEY_NOTI_DATA to id),
        )
    }

    private fun navigateToPostingFragment(id: Int) {
        findNavController().navigate(
            R.id.action_fragment_my_page_to_fragment_posting,
            bundleOf(KEY_NOTI_DATA to id),
        )
    }

    companion object {
        const val ARG_MEMBER_PROFILE = "arg_member_profile"
        const val FROM_FEED = "feed"

        fun newInstance(memberProfile: MyPageModel?): MyPageFeedFragment {
            return MyPageFeedFragment().apply {
                arguments =
                    bundleOf(
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
