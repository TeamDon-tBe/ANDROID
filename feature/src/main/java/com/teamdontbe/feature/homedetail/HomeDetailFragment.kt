package com.teamdontbe.feature.homedetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.drawableOf
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomeDetailBinding
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.dialog.DeleteWithTitleDialogFragment
import com.teamdontbe.feature.home.Feed
import com.teamdontbe.feature.home.HomeAdapter
import com.teamdontbe.feature.home.HomeBottomSheet
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.notification.NotificationFragment.Companion.KEY_NOTI_DATA
import com.teamdontbe.feature.posting.PostingFragment
import com.teamdontbe.feature.snackbar.UploadingSnackBar
import com.teamdontbe.feature.util.Debouncer
import com.teamdontbe.feature.util.EventObserver
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

    private lateinit var homeDetailFeedAdapter: HomeAdapter
    private lateinit var homeDetailFeedCommentAdapter: HomeDetailCommentAdapter

    override fun initView() {
        if ((requireArguments().getInt(KEY_NOTI_DATA)) > 0) {
            homeViewModel.getFeedDetail(requireArguments().getInt(KEY_NOTI_DATA))
            homeViewModel.getCommentList(requireArguments().getInt(KEY_NOTI_DATA))
        }

        getHomeFeedDetailData()?.toFeedEntity()?.contentId?.let { homeViewModel.getCommentList(it) }
        statusBarColorOf(R.color.white)
        initBackBtnClickListener()
        initHomeDetailFeedAdapter()
        (getHomeFeedDetailData())?.let { initInputEditTextClickListener() }
        initEditText()
        initAppbarCancelClickListener()
        initCommentBottomSheet()
        initObserve()
    }

    private fun initHomeDetailFeedAdapter() {
        homeDetailFeedAdapter =
            HomeAdapter(onClickKebabBtn = { feedData, positoin ->
                feedData.contentId?.let {
                    initBottomSheet(
                        feedData.memberId == homeViewModel.getMemberId(),
                        it, false, -1,
                    )
                }
            }).apply {
                submitList(
                    listOf(getHomeFeedDetailData()?.toFeedEntity()),
                )
            }

        contentId = getHomeFeedDetailData()?.contentId ?: -1
    }

    private fun initObserve() {
        homeViewModel.getFeedDetail.flowWithLifecycle(lifecycle).onEach { result ->
            when (result) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    homeDetailFeedAdapter =
                        HomeAdapter(onClickKebabBtn = { feedData, positoin ->
                            feedData.contentId?.let {
                                initBottomSheet(
                                    feedData.memberId == homeViewModel.getMemberId(),
                                    it, false, -1,
                                )
                            }
                        }).apply {
                            submitList(
                                listOf(result.data),
                            )
                        }

                    contentId = result.data.contentId ?: -1

                    if (::homeDetailFeedAdapter.isInitialized && ::homeDetailFeedCommentAdapter.isInitialized) {
                        binding.rvHomeDetail.adapter =
                            ConcatAdapter(homeDetailFeedAdapter, homeDetailFeedCommentAdapter)
                        binding.rvHomeDetail.addItemDecoration(
                            HomeDetailFeedItemDecorator(
                                requireContext(),
                            ),
                        )
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)

        homeViewModel.getCommentList.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    homeDetailFeedCommentAdapter =
                        HomeDetailCommentAdapter(onClickKebabBtn = { commentData, position ->
                            initBottomSheet(
                                commentData.memberId == homeViewModel.getMemberId(),
                                contentId, true, commentData.commentId,
                            )
                            deleteCommentPosition = position
                        }, onClickLikedBtn = { contentId, status ->
                            if (status) {
                                homeViewModel.deleteCommentLiked(contentId)
                            } else {
                                homeViewModel.postCommentLiked(
                                    contentId,
                                )
                            }
                        }).apply {
                            submitList(it.data)
                        }

                    if (::homeDetailFeedAdapter.isInitialized && ::homeDetailFeedCommentAdapter.isInitialized) {
                        binding.rvHomeDetail.adapter =
                            ConcatAdapter(homeDetailFeedAdapter, homeDetailFeedCommentAdapter)
                        binding.rvHomeDetail.addItemDecoration(
                            HomeDetailFeedItemDecorator(
                                requireContext(),
                            ),
                        )
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)

        homeViewModel.postCommentPosting.observe(
            this,
            EventObserver {
                handleCommentPostingSuccess()
            },
        )

        homeViewModel.openDeleteCommentDialog.observe(
            this,
            EventObserver {
                initDeleteCommentDialog(it)
            },
        )

        homeViewModel.deleteComment.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    if (deleteCommentPosition != -1) {
                        homeDetailFeedCommentAdapter.deleteItem(deleteCommentPosition)
                        deleteCommentPosition = -1
                    }
                    val dialog = DeleteCompleteDialogFragment()
                    dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun handleCommentPostingSuccess() {
        requireContext().hideKeyboard(binding.root)
        (requireActivity() as MainActivity).findViewById<View>(R.id.bnv_main).visibility =
            View.VISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        homeViewModel.getCommentList(contentId)
        UploadingSnackBar.make(binding.root).show()
    }

    private fun initBottomSheet(
        isMember: Boolean,
        contentId: Int,
        isComment: Boolean,
        commentId: Int,
    ) {
        HomeBottomSheet(isMember, contentId, isComment, commentId).show(
            childFragmentManager,
            HOME_DETAIL_BOTTOM_SHEET,
        )
    }

    private fun getHomeFeedDetailData(): Feed? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_FEED_DATA, Feed::class.java)
        } else {
            requireArguments().getParcelable(KEY_FEED_DATA) as? Feed
        }

    private fun initBackBtnClickListener() {
        binding.ivHomeDetailBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initInputEditTextClickListener() {
        binding.tvHomeDetailInput.setOnClickListener {
            binding.bottomsheetHomeDetail.etCommentContent.text.clear()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

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

    @SuppressLint("ResourceAsColor")
    private fun initEditText() {
        binding.run {
            bottomsheetHomeDetail.etCommentContent.doAfterTextChanged {
                when {
                    bottomsheetHomeDetail.etCommentContent.text.toString().length in 1..499 -> {
                        bottomsheetHomeDetail.layoutUploadBar.pbUploadBarInput.progressDrawable =
                            context?.drawableOf(R.drawable.shape_primary_line_10_ring)
                        bottomsheetHomeDetail.layoutUploadBar.pbUploadBarInput.progress =
                            bottomsheetHomeDetail.etCommentContent.text.toString().length

                        bottomsheetHomeDetail.layoutUploadBar.btnUploadBarUpload.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.primary,
                                ),
                            )
                        bottomsheetHomeDetail.layoutUploadBar.btnUploadBarUpload.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.black,
                            ),
                        )
                        initUploadingBtnClickListener()
                    }

                    bottomsheetHomeDetail.etCommentContent.text.toString().length >= 500 -> {
                        bottomsheetHomeDetail.layoutUploadBar.pbUploadBarInput.progressDrawable =
                            context?.drawableOf(R.drawable.shape_error_line_10_ring)
                        bottomsheetHomeDetail.layoutUploadBar.pbUploadBarInput.progress =
                            bottomsheetHomeDetail.etCommentContent.text.toString().length

                        bottomsheetHomeDetail.layoutUploadBar.btnUploadBarUpload.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.gray_3,
                                ),
                            )
                        bottomsheetHomeDetail.layoutUploadBar.btnUploadBarUpload.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.gray_9,
                            ),
                        )
                        initUploadingBtnClickListener()
                    }

                    else -> {
                        bottomsheetHomeDetail.layoutUploadBar.pbUploadBarInput.progress = 0
                        bottomsheetHomeDetail.layoutUploadBar.btnUploadBarUpload.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.gray_3,
                                ),
                            )
                        bottomsheetHomeDetail.layoutUploadBar.btnUploadBarUpload.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.gray_9,
                            ),
                        )
                    }
                }
                commentDebouncer.setDelay(
                    bottomsheetHomeDetail.etCommentContent.text.toString(),
                    1000L,
                ) {}
            }
        }
    }

    private fun initUploadingBtnClickListener() {
        binding.bottomsheetHomeDetail.layoutUploadBar.btnUploadBarUpload.setOnClickListener {
            homeViewModel.postCommentPosting(
                contentId,
                binding.bottomsheetHomeDetail.etCommentContent.text.toString(),
            )
        }
    }

    private fun initAppbarCancelClickListener() {
        binding.bottomsheetHomeDetail.tvCommentAppbarCancel.setOnClickListener {
            val dialog = DeleteDialogFragment("작성한 답글을 삭제하시겠어요?")
            dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
        }
    }

    private fun initCommentBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomsheetHomeDetail.root)
        binding.bottomsheetHomeDetail.feed = getHomeFeedDetailData()?.toFeedEntity()
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.expandedOffset = 28
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.isFitToContents = true
    }

    private fun initComplaintDialog(contentId: Int) {
        val dialog =
            DeleteWithTitleDialogFragment(
                getString(R.string.tv_delete_with_title_complain_dialog),
                getString(R.string.tv_delete_with_title_dialog_comment),
                false,
                contentId,
                true,
            )
        dialog.show(childFragmentManager, HOME_DETAIL_BOTTOM_SHEET)
    }

    private fun initDeleteCommentDialog(contentId: Int) {
        val dialog =
            DeleteWithTitleDialogFragment(
                getString(R.string.tv_delete_with_title_delete_comment_dialog),
                getString(R.string.tv_delete_with_title_delete_comment_content_dialog),
                true,
                contentId,
                true,
            )
        dialog.show(childFragmentManager, HOME_DETAIL_BOTTOM_SHEET)
    }

    companion object {
        const val HOME_DETAIL_BOTTOM_SHEET = "home_detail_bottom_sheet"
        const val KEY_FEED_DATA = "key_feed_data"
        const val DELAY = 1000L
        val POSSIBLE_LENGTH = 1..499
        const val MAX_LENGTH = 500
    }
}
