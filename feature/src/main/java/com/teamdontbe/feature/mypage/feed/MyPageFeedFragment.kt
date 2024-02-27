package com.teamdontbe.feature.mypage.feed

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.fragment.viewLifeCycle
import com.teamdontbe.core_ui.util.fragment.viewLifeCycleScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.ErrorActivity.Companion.navigateToErrorPage
import com.teamdontbe.feature.MainActivity
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
import com.teamdontbe.feature.snackbar.TransparentIsGhostSnackBar
import com.teamdontbe.feature.util.FeedItemDecorator
import com.teamdontbe.feature.util.KeyStorage
import com.teamdontbe.feature.util.KeyStorage.KEY_NOTI_DATA
import com.teamdontbe.feature.util.PagingLoadingAdapter
import com.teamdontbe.feature.util.pagingSubmitData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyPageFeedFragment :
    BindingFragment<FragmentMyPageFeedBinding>(R.layout.fragment_my_page_feed) {
    private val myPageFeedViewModel by activityViewModels<MyPageViewModel>()

    private lateinit var memberProfile: MyPageModel
    private var deleteFeedPosition: Int = -1

    private var _myPageFeedAdapter: MyPageFeedAdapter? = null
    private val myPageFeedAdapter: MyPageFeedAdapter
        get() = requireNotNull(_myPageFeedAdapter)

    private var deletedItemCount: Int = 0

    override fun initView() {
        // Arguments에서 memberProfile 값을 가져오기
        initMemberProfile()
        initFeedRecyclerView()
        initDeleteObserve()
        initTransparentObserve()
        stateFeedItemNull()
        scrollRecyclerViewToTop()
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

    private fun initFeedRecyclerView() {
        _myPageFeedAdapter =
            MyPageFeedAdapter(
                context = requireContext(),
                idFlag = memberProfile.idFlag,
                onClickKebabBtn = ::clickKebabBtn,
                onItemClicked = { feedEntity ->
                    navigateToHomeDetailFragment(feedEntity.contentId ?: -1)
                },
                onClickLikedBtn = ::onLikedBtnClick,
                onClickTransparentBtn = ::onTransparentBtnClick,

            ).apply {
                pagingSubmitData(
                    viewLifecycleOwner,
                    myPageFeedViewModel.getMyPageFeedList(memberProfile.id),
                    pagingAdapter = this
                )
            }
        binding.rvMyPagePosting.adapter =
            myPageFeedAdapter.withLoadStateFooter(footer = PagingLoadingAdapter())

        setUpItemDecorator()
    }

    private fun clickKebabBtn(feedEntity: FeedEntity, position: Int) {
        feedEntity.contentId?.let {
            initBottomSheet(
                feedEntity.memberId == myPageFeedViewModel.getMemberId(),
                contentId = it,
                commentId = -1,
                whereFrom = FROM_FEED,
            )
            deleteFeedPosition = position
        }
    }

    private fun onLikedBtnClick(
        contentId: Int,
        status: Boolean,
    ) {
        if (status) {
            myPageFeedViewModel.deleteFeedLiked(contentId)
        } else {
            myPageFeedViewModel.postFeedLiked(contentId)
        }
    }

    private fun onTransparentBtnClick(data: FeedEntity) {
        if (data.isGhost) {
            TransparentIsGhostSnackBar.make(binding.root).show()
        } else {
            initTransparentDialog(data.memberId, data.contentId ?: -1)
        }
    }

    private fun initTransparentDialog(
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) {
        val dialog = MyPageTransparentDialogFragment(
            ALARM_TRIGGER_TYPE_CONTENT,
            targetMemberId,
            alarmTriggerId
        )
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

    private fun setUpItemDecorator() {
        if (binding.rvMyPagePosting.itemDecorationCount == 0) {
            binding.rvMyPagePosting.addItemDecoration(
                FeedItemDecorator(requireContext()),
            )
        }
    }

    private fun initDeleteObserve() {
        myPageFeedViewModel.deleteFeed.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> handleDeleteFeedSuccess()
                is UiState.Failure -> navigateToErrorPage(requireContext())
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun handleDeleteFeedSuccess() {
        if (deleteFeedPosition != -1) {
            myPageFeedAdapter.deleteItem(deleteFeedPosition)
            deleteFeedPosition = -1
        }
        if (deletedItemCount == myPageFeedAdapter.itemCount) {
            stateFeedItemNull()
        }
        deletedItemCount = 0
        val dialog = DeleteCompleteDialogFragment()
        dialog.show(childFragmentManager, KeyStorage.DELETE_POSTING)
    }

    private fun stateFeedItemNull() {
        if (!memberProfile.idFlag) return
        myPageFeedAdapter.addLoadStateListener { combinedLoadStates ->
            val isEmpty =
                myPageFeedAdapter.itemCount == 0 && combinedLoadStates.refresh is LoadState.NotLoading
            if (isEmpty) {
                updateNoFeedUI()
            } else {
                updateExistFeedUi()
            }
        }
    }

    private fun updateNoFeedUI() = with(binding) {
        rvMyPagePosting.visibility = View.GONE
        viewMyPageNoFeedNickname.apply {
            clNoFeed.visibility = View.VISIBLE
            tvNoFeedNickname.text = getString(R.string.my_page_no_feed_text, memberProfile.nickName)
            btnNoFeedPosting.setOnClickListener {
                navigateToPostingFragment(memberProfile.id)
            }
        }
    }

    private fun updateExistFeedUi() {
        binding.viewMyPageNoFeedNickname.clNoFeed.visibility = View.GONE
        binding.rvMyPagePosting.visibility = View.VISIBLE
    }

    private fun initTransparentObserve() {
        myPageFeedViewModel.postTransparent.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> myPageFeedViewModel.getMyPageFeedList(memberProfile.id)
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(viewLifeCycleScope)
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

    private fun scrollRecyclerViewToTop() {
        val mainActivity = requireActivity() as? MainActivity

        mainActivity?.findViewById<BottomNavigationView>(R.id.bnv_main)
            ?.setOnItemReselectedListener { item ->
                if (item.itemId == R.id.fragment_my_page) {
                    binding.rvMyPagePosting.post {
                        binding.rvMyPagePosting.smoothScrollToPosition(0)
                    }
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _myPageFeedAdapter = null
    }
}
