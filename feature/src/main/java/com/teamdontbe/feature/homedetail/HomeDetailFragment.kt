package com.teamdontbe.feature.homedetail

import android.content.Intent
import android.os.Build
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.AmplitudeUtil.trackEvent
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.util.fragment.viewLifeCycle
import com.teamdontbe.core_ui.util.fragment.viewLifeCycleScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.ErrorActivity
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomeDetailBinding
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.dialog.TransparentDialogFragment
import com.teamdontbe.feature.home.Feed
import com.teamdontbe.feature.home.HomeBottomSheet
import com.teamdontbe.feature.home.HomeFeedAdapter
import com.teamdontbe.feature.home.HomeFragment
import com.teamdontbe.feature.home.HomeFragment.Companion.KEY_HOME_DETAIL_FEED
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.snackbar.LinkCountErrorSnackBar
import com.teamdontbe.feature.snackbar.TransparentIsGhostSnackBar
import com.teamdontbe.feature.snackbar.UploadingSnackBar
import com.teamdontbe.feature.util.AmplitudeTag.CLICK_POST_LIKE
import com.teamdontbe.feature.util.AmplitudeTag.CLICK_REPLY_LIKE
import com.teamdontbe.feature.util.BottomSheetTag.COMMENT
import com.teamdontbe.feature.util.DialogTag.DELETE_COMPLETE
import com.teamdontbe.feature.util.KeyStorage.KEY_NOTI_DATA
import com.teamdontbe.feature.util.PagingLoadingAdapter
import com.teamdontbe.feature.util.pagingSubmitData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeDetailFragment :
    BindingFragment<FragmentHomeDetailBinding>(R.layout.fragment_home_detail) {
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private var contentId: Int = -1
    private var deleteCommentPosition: Int = -1

    private lateinit var homeFeedAdapter: HomeFeedAdapter
    private lateinit var homeDetailCommentAdapter: HomeDetailPagingCommentAdapter

    override fun initView() {
        binding.vm = homeViewModel
        binding.root.context.hideKeyboard(binding.root)
        statusBarColorOf(R.color.white)
        observeGetFeedDetail()
        getHomeDetail()
        handleGetContentError()
        observeDeleteFeed()
        observeDeleteComment()
        observePostTransparent()
        observePostCommentPosting()
        initBackBtnClickListener()
        initInputEditTextClickListener()
    }

    private fun getHomeDetail() {
        val newContentId = requireArguments().getInt(KEY_NOTI_DATA)

        if (newContentId > 0) {
            contentId = newContentId
            homeViewModel.getFeedDetail(requireArguments().getInt(KEY_NOTI_DATA))
            initHomeDetailCommentAdapter(requireArguments().getInt(KEY_NOTI_DATA))
        } else {
            getHomeFeedDetailData()?.toFeedEntity()?.let {
                initHomeFeedAdapter(listOf(it))
                contentId = it.contentId ?: return
                initHomeDetailCommentAdapter(contentId)
                binding.feed = it
            }
        }
    }

    private fun getHomeFeedDetailData(): Feed? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_HOME_DETAIL_FEED, Feed::class.java)
        } else {
            requireArguments().getParcelable(KEY_HOME_DETAIL_FEED) as? Feed
        }

    private fun initHomeDetailCommentAdapter(contentId: Int) {
        homeDetailCommentAdapter = HomeDetailPagingCommentAdapter(
            context = requireContext(),
            onClickKebabBtn = { commentData, position ->
                onKebabBtnClick(
                    commentData.memberId,
                    -1,
                    commentData.commentId,
                    true,
                    position,
                )
            },
            onClickLikedBtn = ::onCommentLikedBtnClick,
            onClickTransparentBtn = { commentData ->
                onTransparentBtnClick(
                    commentData.isGhost,
                    commentData.memberId,
                    commentData.commentId,
                    ALARM_TRIGGER_TYPE_COMMENT,
                )
            },
            onClickUserProfileBtn = { memberId -> navigateToMyPageFragment(memberId) },
            userId = homeViewModel.getMemberId(),
        )
        initHomeDetailAdapterPagingData(contentId)
        concatFeedCommentAdapter()
    }

    private fun initHomeDetailAdapterPagingData(contentId: Int) {
        homeDetailCommentAdapter.apply {
            pagingSubmitData(
                viewLifecycleOwner,
                homeViewModel.getCommentList(contentId),
                homeDetailCommentAdapter
            )
        }
    }

    private fun initHomeFeedAdapter(feedListData: List<FeedEntity>) {
        homeFeedAdapter = HomeFeedAdapter(
            context = requireContext(),
            onClickKebabBtn = { feedData, position ->
                onKebabBtnClick(
                    feedData.memberId,
                    feedData.contentId ?: -1,
                    -1,
                    false,
                    position,
                )
            },
            onClickLikedBtn = ::onFeedLikedBtnClick,
            onClickTransparentBtn = { feedData ->
                onTransparentBtnClick(
                    feedData.isGhost,
                    feedData.memberId,
                    feedData.contentId,
                    ALARM_TRIGGER_TYPE_CONTENT,
                )
            },
            onClickUserProfileBtn = { memberId -> navigateToMyPageFragment(memberId) },
            userId = homeViewModel.getMemberId(),
            onClickToNavigateToHomeDetail = {},
        ).apply {
            submitList(feedListData)
        }
    }

    private fun onKebabBtnClick(
        memberId: Int,
        contentId: Int,
        commentId: Int,
        isComment: Boolean,
        position: Int,
    ) {
        initBottomSheet(
            memberId == homeViewModel.getMemberId(),
            contentId,
            isComment,
            commentId,
        )
        if (isComment) deleteCommentPosition = position
    }

    private fun initBottomSheet(
        isMember: Boolean,
        contentId: Int,
        isComment: Boolean,
        commentId: Int,
    ) {
        HomeBottomSheet(isMember, contentId, isComment, commentId).show(
            parentFragmentManager,
            HomeFragment.HOME_BOTTOM_SHEET,
        )
    }

    private fun onFeedLikedBtnClick(
        id: Int,
        status: Boolean,
    ) {
        if (status) {
            homeViewModel.deleteFeedLiked(id)
        } else {
            trackEvent(CLICK_POST_LIKE)
            homeViewModel.postFeedLiked(id)
        }
    }

    private fun onCommentLikedBtnClick(
        id: Int,
        status: Boolean,
    ) {
        if (status) {
            homeViewModel.deleteCommentLiked(id)
        } else {
            trackEvent(CLICK_REPLY_LIKE)
            homeViewModel.postCommentLiked(id)
        }
    }

    private fun onTransparentBtnClick(
        isGhost: Boolean,
        memberId: Int,
        alarmTriggerId: Int?,
        alarmTriggerType: String,
    ) {
        if (isGhost) {
            TransparentIsGhostSnackBar.make(binding.root).show()
        } else {
            initTransparentDialog(alarmTriggerType, memberId, alarmTriggerId ?: -1)
        }
    }

    private fun initTransparentDialog(
        alarmTriggerType: String,
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) {
        val dialog = TransparentDialogFragment(alarmTriggerType, targetMemberId, alarmTriggerId)
        dialog.show(childFragmentManager, HomeFragment.HOME_TRANSPARENT_DIALOG)
    }

    private fun navigateToMyPageFragment(memberId: Int) {
        findNavController().navigate(
            R.id.action_fragment_home_detail_to_fragment_my_page,
            bundleOf(HomeFragment.KEY_FEED_DATA to memberId),
        )
    }

    private fun handleGetContentError() {
        homeDetailCommentAdapter.addLoadStateListener { state ->
            when (state.refresh) {
                is LoadState.Error -> navigateToErrorPage()
                else -> Unit
            }
        }
    }

    private fun observeGetFeedDetail() {
        homeViewModel.getFeedDetail.flowWithLifecycle(viewLifeCycle).onEach { result ->
            when (result) {
                is UiState.Success -> {
                    initHomeFeedAdapter(listOf(result.data.copy(contentId = contentId)))
                    binding.feed = result.data
                    concatFeedCommentAdapter()
                }

                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun concatFeedCommentAdapter() {
        if (::homeFeedAdapter.isInitialized && ::homeDetailCommentAdapter.isInitialized) {
            setRecyclerViewItemDecoration()
            binding.rvHomeDetail.adapter = ConcatAdapter(
                homeFeedAdapter,
                homeDetailCommentAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadingAdapter(),
                    footer = PagingLoadingAdapter()
                )
            )
        }
    }

    private fun setRecyclerViewItemDecoration() {
        if (binding.rvHomeDetail.itemDecorationCount == 0) {
            binding.rvHomeDetail.addItemDecoration(
                HomeDetailFeedItemDecorator(requireContext()),
            )
        }
    }

    private fun navigateToErrorPage() {
        startActivity(Intent(requireActivity(), ErrorActivity::class.java))
    }

    private fun observePostCommentPosting() {
        homeViewModel.postCommentPosting.flowWithLifecycle(viewLifeCycle).onEach { result ->
            when (result) {
                is UiState.Success -> handleCommentPostingSuccess()
                is UiState.Failure -> {
                    LinkCountErrorSnackBar.make(binding.root).apply {
                        setText(result.msg)
                        show()
                    }
                }

                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun handleCommentPostingSuccess() {
        homeDetailCommentAdapter.refresh()
        binding.rvHomeDetail.post {
            binding.rvHomeDetail.smoothScrollToPosition(homeDetailCommentAdapter.itemCount + 1)
        }
        requireContext().hideKeyboard(binding.root)
        (requireActivity() as MainActivity).findViewById<View>(R.id.bnv_main).visibility =
            View.VISIBLE
        UploadingSnackBar.make(binding.root).show()
    }

    private fun observeDeleteComment() {
        homeViewModel.deleteComment.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> handleDeleteCommentSuccess()
                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun handleDeleteCommentSuccess() {
        homeDetailCommentAdapter.refresh()
        val dialog = DeleteCompleteDialogFragment()
        dialog.show(childFragmentManager, DELETE_COMPLETE)
    }

    private fun observeDeleteFeed() {
        homeViewModel.deleteFeed.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> findNavController().navigateUp()
                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun observePostTransparent() {
        homeViewModel.postTransparent.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> {
                    homeViewModel.getFeedDetail(contentId)
                    homeDetailCommentAdapter.refresh()
                }

                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initBackBtnClickListener() {
        binding.ivHomeDetailBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initInputEditTextClickListener() {
        binding.tvHomeDetailInput.setOnClickListener {
            binding.feed?.let { initCommentBottomSheet(contentId, it) }
        }
    }

    private fun initCommentBottomSheet(
        contentId: Int,
        feed: FeedEntity
    ) {
        CommentBottomSheet(contentId, feed).show(
            parentFragmentManager,
            COMMENT,
        )
    }

    companion object {
        const val COMMENT_DEBOUNCE_DELAY = 1000L
        const val ALARM_TRIGGER_TYPE_COMMENT = "commentGhost"
        const val ALARM_TRIGGER_TYPE_CONTENT = "contentGhost"
    }
}
