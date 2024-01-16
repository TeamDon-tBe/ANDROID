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
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.dialog.DeleteWithTitleDialogFragment
import com.teamdontbe.feature.posting.PostingFragment
import com.teamdontbe.feature.util.EventObserver
import com.teamdontbe.feature.util.FeedItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun initView() {
        homeViewModel.getFeedList()
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

        homeViewModel.deleteFeed.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    val dialog = DeleteCompleteDialogFragment()
                    dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)

        homeViewModel.openComplaintDialog.observe(
            viewLifecycleOwner,
            EventObserver {
                initComplaintDialog(it)
                Timber.d("ttt", it)
            },
        )

        homeViewModel.openDeleteDialog.observe(
            viewLifecycleOwner,
            EventObserver {
                initDeleteDialog(it)
                Timber.d("ttt", it)
            },
        )
    }

    private fun initHomeAdapter(feedData: List<FeedEntity>) {
        binding.rvHome.adapter =
            HomeAdapter(onClickKebabBtn = { feedData, positoin ->
                feedData.contentId?.let {
                    initBottomSheet(
                        feedData.memberId == homeViewModel.getMemberId(),
                        it,
                    )
                }
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
                        feedData.contentId,
                    ),
                )
            }).apply {
                submitList(feedData)
            }

        binding.rvHome.addItemDecoration(FeedItemDecorator(requireContext()))
    }

    private fun initBottomSheet(
        isMember: Boolean,
        contentId: Int,
    ) {
        HomeBottomSheet(isMember, contentId).show(parentFragmentManager, HOME_BOTTOM_SHEET)
    }

    private fun navigateToHomeDetailFragment(feedData: Feed) {
        findNavController().navigate(
            R.id.action_home_to_home_detail,
            bundleOf(KEY_FEED_DATA to feedData),
        )
    }

    private fun initComplaintDialog(contentId: Int) {
        val dialog =
            DeleteWithTitleDialogFragment(
                "신고하시겠어요?",
                "해당 유저 혹은 게시글을 신고하시려면 신고하기 버튼을 눌러주세요.",
                false,
                contentId,
            )
        dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
    }

    private fun initDeleteDialog(contentId: Int) {
        val dialog =
            DeleteWithTitleDialogFragment(
                "게시글을 삭제하시겠어요?",
                "삭제된 게시글은 영구히 사라져요.",
                true,
                contentId,
            )
        dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
    }

    companion object {
        const val HOME_BOTTOM_SHEET = "home_bottom_sheet"
        const val KEY_FEED_DATA = "key_feed_data"
    }
}
