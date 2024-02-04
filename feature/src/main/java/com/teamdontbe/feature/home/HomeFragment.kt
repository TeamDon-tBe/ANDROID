package com.teamdontbe.feature.home

import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomeBinding
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.dialog.TransparentDialogFragment
import com.teamdontbe.feature.posting.PostingFragment
import com.teamdontbe.feature.snackbar.TransparentIsGhostSnackBar
import com.teamdontbe.feature.util.FeedItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel by activityViewModels<HomeViewModel>()

    private lateinit var homeAdapter: HomeAdapter
    private var deleteFeedPosition: Int = -1

    override fun initView() {
        homeViewModel.getFeedList()
        collectFeedList()
        collectDeleteFeedStatus()
        collectPostTransparentStatus()
        initSwipeRefreshData()
        scrollRecyclerViewToTop()
    }

    private fun initSwipeRefreshData() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            homeViewModel.getFeedList()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun collectFeedList()  {
        homeViewModel.getFeedList.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> initHomeAdapter(it.data)
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun collectDeleteFeedStatus()  {
        homeViewModel.deleteFeed.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {
                    if (deleteFeedPosition != -1) {
                        homeAdapter.deleteItem(deleteFeedPosition)
                        deleteFeedPosition = -1
                    }
                    val dialog = DeleteCompleteDialogFragment()
                    dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
                }
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun collectPostTransparentStatus()  {
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
            HomeAdapter(
                onClickKebabBtn = { feedData, positoin ->
                    feedData.contentId?.let {
                        initBottomSheet(
                            feedData.memberId == homeViewModel.getMemberId(),
                            it,
                            false,
                            -1,
                        )
                        deleteFeedPosition = positoin
                    }
                },
                onClickToNavigateToHomeDetail = { feedData, position ->
                    navigateToHomeDetailFragment(
                        Feed(
                            feedData.memberId,
                            feedData.memberProfileUrl,
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
                },
                onClickLikedBtn = { contentId, status ->
                    Timber.tag("status").d(status.toString())
                    if (status) {
                        homeViewModel.deleteFeedLiked(contentId)
                    } else {
                        homeViewModel.postFeedLiked(
                            contentId,
                        )
                    }
                },
                onClickTransparentBtn = { data, position ->
                    if (position == -2) {
                        TransparentIsGhostSnackBar.make(binding.root).show()
                    } else {
                        initTransparentDialog(data.memberId, data.contentId ?: -1)
                    }
                },
                onClickUserProfileBtn = { feedData, positoin ->
                    feedData.contentId?.let {
                        navigateToMyPageFragment(feedData.memberId)
                    }
                },
                userId = homeViewModel.getMemberId(),
            ).apply {
                submitList(feedData)
            }
        binding.rvHome.adapter = homeAdapter
        if (binding.rvHome.itemDecorationCount == 0) {
            binding.rvHome.addItemDecoration(
                FeedItemDecorator(requireContext()),
            )
        }
    }

    private fun


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
        onDestroy()
    }

    private fun navigateToMyPageFragment(id: Int) {
        findNavController().navigate(
            R.id.action_fragment_home_to_fragment_my_page,
            bundleOf(KEY_FEED_DATA to id),
        )
    }

    private fun initTransparentDialog(
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) {
        val dialog = TransparentDialogFragment(targetMemberId, alarmTriggerId)
        dialog.show(childFragmentManager, HOME_TRANSPARENT_DIALOG)
    }

    private fun scrollRecyclerViewToTop() {
        val mainActivity = requireActivity() as? MainActivity
        val nestedScrollMyPage = binding?.nestedScrollHome

        mainActivity?.findViewById<BottomNavigationView>(R.id.bnv_main)
            ?.setOnItemReselectedListener { item ->
                if (item.itemId == R.id.fragment_home && nestedScrollMyPage != null) {
                    nestedScrollMyPage.post {
                        nestedScrollMyPage.smoothScrollTo(0, 0)
                    }
                }
            }
    }

    companion object {
        const val HOME_BOTTOM_SHEET = "home_bottom_sheet"
        const val HOME_TRANSPARENT_DIALOG = "home_transparent_dialog"
        const val KEY_FEED_DATA = "key_feed_data"
    }
}
