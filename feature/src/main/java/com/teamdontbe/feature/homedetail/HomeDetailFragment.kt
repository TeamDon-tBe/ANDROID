package com.teamdontbe.feature.homedetail

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.fragment.colorOf
import com.teamdontbe.core_ui.util.fragment.drawableOf
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.core_ui.view.setOnDuplicateBlockClick
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.ErrorActivity
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomeDetailBinding
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.dialog.TransparentDialogFragment
import com.teamdontbe.feature.home.Feed
import com.teamdontbe.feature.home.HomeBottomSheet
import com.teamdontbe.feature.home.HomeFeedAdapter
import com.teamdontbe.feature.home.HomeFragment
import com.teamdontbe.feature.home.HomeFragment.Companion.KEY_HOME_DETAIL_FEED
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.posting.AnimateProgressBar
import com.teamdontbe.feature.snackbar.TransparentIsGhostSnackBar
import com.teamdontbe.feature.snackbar.UploadingSnackBar
import com.teamdontbe.feature.util.Debouncer
import com.teamdontbe.feature.util.EventObserver
import com.teamdontbe.feature.util.KeyStorage.DELETE_POSTING
import com.teamdontbe.feature.util.KeyStorage.KEY_NOTI_DATA
import com.teamdontbe.feature.util.PagingLoadingAdapter
import com.teamdontbe.feature.util.pagingSubmitData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeDetailFragment :
    BindingFragment<FragmentHomeDetailBinding>(R.layout.fragment_home_detail) {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val commentDebouncer = Debouncer<String>()
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private var contentId: Int = -1

    private var deleteCommentPosition: Int = -1

    private lateinit var homeFeedAdapter: HomeFeedAdapter
    private lateinit var homeDetailCommentAdapter: HomeDetailPagingCommentAdapter

    override fun initView() {
        binding.root.context.hideKeyboard(binding.root)
        statusBarColorOf(R.color.white)
        observeGetFeedDetail()
        getHomeDetail()
        observeDeleteFeed()
        observeDeleteComment()
        observePostTransparent()
        observePostCommentPosting()
        initBackBtnClickListener()
        initEditText()
        initInputEditTextClickListener()
        initAppbarCancelClickListener()
        initCommentBottomSheet()
    }

    private fun getHomeDetail() {
        val newContentId = requireArguments().getInt(KEY_NOTI_DATA)

        if (newContentId > 0) {
            contentId = newContentId
            initHomeDetailCommentAdapter(requireArguments().getInt(KEY_NOTI_DATA))
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
        initHomeDetailAdapterPagingData()
        concatFeedCommentAdapter()
    }

    private fun initHomeDetailAdapterPagingData() {
        homeDetailCommentAdapter.apply {
            pagingSubmitData(
                viewLifecycleOwner,
                homeViewModel.getCommentList(contentId),
                homeDetailCommentAdapter
            )
        }.withLoadStateHeaderAndFooter(
            header = PagingLoadingAdapter(),
            footer = PagingLoadingAdapter()
        )
    }

    private fun initHomeFeedAdapter(feedListData: List<FeedEntity>) {
        homeFeedAdapter = HomeFeedAdapter(
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

    private fun observeGetFeedDetail() {
        homeViewModel.getFeedDetail.flowWithLifecycle(lifecycle).onEach { result ->
            when (result) {
                is UiState.Success -> {
                    initHomeFeedAdapter(listOf(result.data))
                    concatFeedCommentAdapter()
                    binding.feed = result.data
                }

                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun concatFeedCommentAdapter() {
        if (::homeFeedAdapter.isInitialized && ::homeDetailCommentAdapter.isInitialized) {
            binding.rvHomeDetail.adapter = ConcatAdapter(homeFeedAdapter, homeDetailCommentAdapter)
            setRecyclerViewItemDecoration()
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
        homeViewModel.postCommentPosting.observe(
            this,
            EventObserver {
                handleCommentPostingSuccess()
            },
        )
    }

    private fun handleCommentPostingSuccess() {
        getHomeDetail()
        requireContext().hideKeyboard(binding.root)
        (requireActivity() as MainActivity).findViewById<View>(R.id.bnv_main).visibility =
            View.VISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        UploadingSnackBar.make(binding.root).show()
    }

    private fun observeDeleteComment() {
        homeViewModel.deleteComment.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> handleDeleteCommentSuccess()
                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun handleDeleteCommentSuccess() {
        if (deleteCommentPosition != -1) {
            homeDetailCommentAdapter.deleteItem(deleteCommentPosition)
            deleteCommentPosition = -1
        }
        val dialog = DeleteCompleteDialogFragment()
        dialog.show(childFragmentManager, DELETE_POSTING)
    }

    private fun observeDeleteFeed() {
        homeViewModel.deleteFeed.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> findNavController().navigateUp()
                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun observePostTransparent() {
        homeViewModel.postTransparent.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Success -> {
                    homeViewModel.getFeedDetail(contentId)
                    homeDetailCommentAdapter.refresh()
                }

                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initBackBtnClickListener() {
        binding.ivHomeDetailBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initInputEditTextClickListener() {
        binding.tvHomeDetailInput.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isDraggable = false

            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                binding.bottomsheetHomeDetail.etCommentContent,
                InputMethodManager.SHOW_IMPLICIT,
            )

            binding.bottomsheetHomeDetail.etCommentContent.requestFocus()

            (requireActivity() as MainActivity).findViewById<View>(R.id.bnv_main).visibility =
                View.GONE
        }
    }

    private fun initEditText() {
        binding.bottomsheetHomeDetail.etCommentContent.doAfterTextChanged { text ->
            val textLength = text?.length ?: 0
            val progressBarDrawableId = getProgressBarDrawableId(textLength)
            val btnBackgroundTint = getButtonBackgroundTint(textLength)
            val btnTextColor = getButtonTextColor(textLength)

            updateUploadBar(textLength, progressBarDrawableId, btnBackgroundTint, btnTextColor)
            initUploadingBtnClickListener(textLength)
            startProgressBarAnimation(textLength)
            debounceComment(text.toString())
        }
    }

    private fun getProgressBarDrawableId(textLength: Int): Int =
        if (textLength >= MAX_COMMENT_LENGTH) R.drawable.shape_error_line_circle else R.drawable.shape_primary_line_circle

    private fun getButtonBackgroundTint(textLength: Int): ColorStateList =
        if (textLength in MIN_COMMENT_LENGTH until MAX_COMMENT_LENGTH) {
            ColorStateList.valueOf(
                colorOf(R.color.primary),
            )
        } else {
            ColorStateList.valueOf(colorOf(R.color.gray_3))
        }

    private fun getButtonTextColor(textLength: Int): Int =
        if (textLength in MIN_COMMENT_LENGTH until MAX_COMMENT_LENGTH) {
            colorOf(R.color.black)
        } else {
            colorOf(
                R.color.gray_9,
            )
        }

    private fun updateUploadBar(
        textLength: Int,
        progressBarDrawableId: Int,
        btnBackgroundTint: ColorStateList,
        btnTextColor: Int,
    ) {
        with(binding.bottomsheetHomeDetail.layoutUploadBar) {
            pbUploadBarInput.progressDrawable = drawableOf(progressBarDrawableId)
            pbUploadBarInput.progress = textLength
            btnUploadBarUpload.backgroundTintList = btnBackgroundTint
            btnUploadBarUpload.setTextColor(btnTextColor)
        }
    }

    private fun startProgressBarAnimation(textLength: Int) {
        val uploadBar = binding.bottomsheetHomeDetail.layoutUploadBar
        val animateProgressBar = AnimateProgressBar(
            uploadBar.pbUploadBarInput,
            0f,
            textLength.toFloat(),
        )
        uploadBar.pbUploadBarInput.startAnimation(animateProgressBar)
    }

    private fun debounceComment(commentText: String) {
        commentDebouncer.setDelay(commentText, COMMENT_DEBOUNCE_DELAY) {}
    }

    private fun initUploadingBtnClickListener(textLength: Int) {
        binding.bottomsheetHomeDetail.layoutUploadBar.btnUploadBarUpload.setOnDuplicateBlockClick {
            if (textLength in MIN_COMMENT_LENGTH until MAX_COMMENT_LENGTH) {
                homeViewModel.postCommentPosting(
                    contentId,
                    binding.bottomsheetHomeDetail.etCommentContent.text.toString(),
                )
            }
        }
    }

    private fun initAppbarCancelClickListener() {
        binding.bottomsheetHomeDetail.tvCommentAppbarCancel.setOnClickListener {
            val dialog = DeleteDialogFragment(getString(R.string.comment_delete_dialog))
            dialog.show(childFragmentManager, DELETE_POSTING)
        }
    }

    private fun initCommentBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomsheetHomeDetail.root)
        binding.bottomsheetHomeDetail.tvCommentProfileNickname.text =
            homeViewModel.getUserNickname()
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.expandedOffset = 28
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.skipCollapsed = true
    }

    companion object {
        const val HOME_DETAIL_BOTTOM_SHEET = "home_detail_bottom_sheet"
        const val MAX_COMMENT_LENGTH = 500
        const val MIN_COMMENT_LENGTH = 1
        const val COMMENT_DEBOUNCE_DELAY = 1000L
        const val ALARM_TRIGGER_TYPE_COMMENT = "commentGhost"
        const val ALARM_TRIGGER_TYPE_CONTENT = "contentGhost"
    }
}
