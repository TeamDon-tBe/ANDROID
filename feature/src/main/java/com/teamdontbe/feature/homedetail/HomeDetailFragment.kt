package com.teamdontbe.feature.homedetail

import android.os.Build
import android.view.View
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
import com.teamdontbe.core_ui.util.context.openKeyboard
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.comment.UploadingSnackBar
import com.teamdontbe.feature.databinding.FragmentHomeDetailBinding
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.dialog.DeleteWithTitleDialogFragment
import com.teamdontbe.feature.home.Feed
import com.teamdontbe.feature.home.HomeAdapter
import com.teamdontbe.feature.home.HomeBottomSheet
import com.teamdontbe.feature.home.HomeFragment
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.notification.NotificationFragment.Companion.KEY_NOTI_DATA
import com.teamdontbe.feature.posting.PostingFragment
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
                        it, false,
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
                                    it, false,
                                )
                            }
                        }).apply {
                            submitList(
                                listOf(result.data),
                            )
                        }
                    homeDetailFeedAdapter.notifyDataSetChanged()
                    contentId = result.data.contentId ?: -1
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
                        HomeDetailCommentAdapter(onClickKebabBtn = { feedData, position ->
                            initBottomSheet(
                                feedData.memberId == homeViewModel.getMemberId(),
                                contentId, true,
                            )
                            deleteCommentPosition = position
                        }).apply {
                            submitList(it.data)
                        }

                    binding.rvHomeDetail.adapter =
                        ConcatAdapter(homeDetailFeedAdapter, homeDetailFeedCommentAdapter)
                    binding.rvHomeDetail.addItemDecoration(
                        HomeDetailFeedItemDecorator(
                            requireContext(),
                        ),
                    )
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)

        homeViewModel.postCommentPosting.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> handleCommentPostingSuccess()
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)

        homeViewModel.openDeleteCommentDialog.observe(
            viewLifecycleOwner,
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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        (requireActivity() as MainActivity).findViewById<View>(R.id.bnv_main).visibility =
            View.VISIBLE
        homeViewModel.getCommentList(contentId)
        UploadingSnackBar.make(binding.root).show()
    }

    private fun initBottomSheet(
        isMember: Boolean,
        contentId: Int,
        isComment: Boolean,
    ) {
        HomeBottomSheet(isMember, contentId, isComment).show(
            parentFragmentManager,
            HomeFragment.HOME_BOTTOM_SHEET,
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
            binding.bottomsheet.etCommentContent.text.clear()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            (requireActivity() as MainActivity).findViewById<View>(R.id.bnv_main).visibility =
                View.GONE
            binding.bottomsheet.etCommentContent.requestFocus()
            requireContext().openKeyboard(binding.root)
        }
    }

    private fun initEditText() {
        binding.run {
            bottomsheet.etCommentContent.doAfterTextChanged {
                when {
                    bottomsheet.etCommentContent.text.toString().length in POSSIBLE_LENGTH -> {
                        updateUploadBar(
                            R.drawable.shape_primary_line_10_ring,
                            bottomsheet.etCommentContent.text.toString().length,
                            R.drawable.ic_uploading_activate,
                        )
                        initUploadingBtnClickListener()
                    }

                    bottomsheet.etCommentContent.text.toString().length >= MAX_LENGTH -> {
                        updateUploadBar(
                            R.drawable.shape_error_line_10_ring,
                            bottomsheet.etCommentContent.text.toString().length,
                            R.drawable.ic_uploading_deactivate,
                        )
                    }

                    else -> {
                        bottomsheet.layoutUploadBar.pbUploadBarInput.progress = 0
                        bottomsheet.layoutUploadBar.btnUploadBarUpload.setImageResource(R.drawable.ic_uploading_deactivate)
                    }
                }
                commentDebouncer.setDelay(
                    binding.bottomsheet.etCommentContent.text.toString(),
                    DELAY,
                ) {}
            }
        }
    }

    private fun updateUploadBar(
        progressDrawableResId: Int,
        progress: Int,
        imageResourceResId: Int,
    ) {
        with(binding) {
            bottomsheet.layoutUploadBar.pbUploadBarInput.progressDrawable =
                context?.drawableOf(progressDrawableResId)
            bottomsheet.layoutUploadBar.pbUploadBarInput.progress = progress
            bottomsheet.layoutUploadBar.btnUploadBarUpload.setImageResource(imageResourceResId)
        }
    }

    private fun initUploadingBtnClickListener() {
        binding.bottomsheet.layoutUploadBar.btnUploadBarUpload.setOnClickListener {
            homeViewModel.postCommentPosting(
                contentId,
                binding.bottomsheet.etCommentContent.text.toString(),
            )
        }
    }

    private fun initAppbarCancelClickListener() {
        binding.bottomsheet.tvCommentAppbarCancel.setOnClickListener {
            val dialog = DeleteDialogFragment("작성한 답글을 삭제하시겠어요?")
            dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
        }
    }

    private fun initCommentBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomsheet.root)
        binding.bottomsheet.feed = getHomeFeedDetailData()?.toFeedEntity()
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.expandedOffset = 28
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.isFitToContents = false
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
        dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
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
        dialog.show(childFragmentManager, PostingFragment.DELETE_POSTING)
    }

    companion object {
        const val KEY_FEED_DATA = "key_feed_data"
        const val DELAY = 1000L
        val POSSIBLE_LENGTH = 1..499
        const val MAX_LENGTH = 500
    }
}
