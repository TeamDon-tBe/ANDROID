package com.teamdontbe.feature.home

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
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
import com.teamdontbe.feature.dialog.TransparentDialogFragment
import com.teamdontbe.feature.posting.PostingFragment
import com.teamdontbe.feature.snackbar.TransparentIsGhostSnackBar
import com.teamdontbe.feature.util.EventObserver
import com.teamdontbe.feature.util.FeedItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel by activityViewModels<HomeViewModel>()

    private lateinit var homeAdapter: HomeAdapter
    private var deleteFeedPosition: Int = -1
    private var updateFeedPosition: Int = -1

    override fun initView() {
        homeViewModel.getFeedList()
        initObserve()
        initSwipeRefreshData()
    }

    private fun initSwipeRefreshData() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.getFeedList()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initObserve() {
        homeViewModel.getFeedList.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> binding.progressbarHome.isVisible = true
                is UiState.Success -> {
                    binding.progressbarHome.isVisible = false
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
                    if (deleteFeedPosition != -1) {
                        homeAdapter.deleteItem(deleteFeedPosition)
                        deleteFeedPosition = -1
                    }
                    val dialog = DeleteCompleteDialogFragment()
                    dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)

        homeViewModel.openDeleteDialog.observe(
            this,
            EventObserver {
                initDeleteDialog(it)
            },
        )

        homeViewModel.openComplaintDialog.observe(
            this,
            EventObserver {
                initComplaintDialog(it)
            },
        )

        homeViewModel.postTransparent.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> homeViewModel.getFeedList()
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initHomeAdapter(feedData: List<FeedEntity>) {
        homeAdapter =
            HomeAdapter(onClickKebabBtn = { feedData, positoin ->
                feedData.contentId?.let {
                    initBottomSheet(
                        feedData.memberId == homeViewModel.getMemberId(),
                        it, false, -1,
                    )
                    deleteFeedPosition = positoin
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
            }, onClickLikedBtn = { contentId, status ->
                if (status) {
                    homeViewModel.deleteFeedLiked(contentId)
                } else {
                    homeViewModel.postFeedLiked(
                        contentId,
                    )
                }
            }, onClickTransparentBtn = { data, position ->
                if (position == -2) {
                    TransparentIsGhostSnackBar.make(binding.root).show()
                } else {
                    initTransparentDialog(data.memberId, data.contentId ?: -1)
                    updateFeedPosition = position
                }
            }).apply {
                submitList(feedData)
            }
        binding.rvHome.adapter = homeAdapter
        if (binding.rvHome.itemDecorationCount == 0) {
            binding.rvHome.addItemDecoration(
                FeedItemDecorator(requireContext()),
            )
        }
    }

    private fun initBottomSheet(
        isMember: Boolean,
        contentId: Int,
        isComment: Boolean,
        commentId: Int,
    ) {
        HomeBottomSheet(isMember, contentId, isComment, commentId).show(
            parentFragmentManager,
            HOME_BOTTOM_SHEET,
        )
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
                getString(R.string.tv_delete_with_title_complain_dialog),
                getString(R.string.tv_delete_with_title_dialog_content),
                false,
                contentId,
                false,
            )
        dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
    }

    private fun initDeleteDialog(contentId: Int) {
        val dialog =
            DeleteWithTitleDialogFragment(
                getString(R.string.tv_delete_with_title_delete_dialog),
                getString(R.string.tv_delete_with_title_delete_content_dialog),
                true,
                contentId,
                false,
            )
        dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
    }

    private fun initTransparentDialog(
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) {
        val dialog = TransparentDialogFragment(targetMemberId, alarmTriggerId)
        dialog.show(childFragmentManager, HOME_TRANSPARENT_DIALOG)
    }

    companion object {
        const val HOME_BOTTOM_SHEET = "home_bottom_sheet"
        const val HOME_TRANSPARENT_DIALOG = "home_transparent_dialog"
        const val KEY_FEED_DATA = "key_feed_data"
    }
}
