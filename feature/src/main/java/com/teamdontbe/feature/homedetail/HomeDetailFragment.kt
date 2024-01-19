package com.teamdontbe.feature.homedetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.drawableOf
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.ErrorActivity
import com.teamdontbe.feature.MainActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomeDetailBinding
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.dialog.DeleteDialogFragment
import com.teamdontbe.feature.dialog.TransparentDialogFragment
import com.teamdontbe.feature.home.Feed
import com.teamdontbe.feature.home.HomeAdapter
import com.teamdontbe.feature.home.HomeBottomSheet
import com.teamdontbe.feature.home.HomeFragment
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.notification.NotificationFragment.Companion.KEY_NOTI_DATA
import com.teamdontbe.feature.posting.AnimateProgressBar
import com.teamdontbe.feature.posting.PostingFragment
import com.teamdontbe.feature.snackbar.TransparentIsGhostSnackBar
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
    private var updateFeedPosition: Int = -1

    private lateinit var homeDetailFeedAdapter: HomeAdapter
    private lateinit var homeDetailFeedCommentAdapter: HomeDetailCommentAdapter

    override fun initView() {
        binding.ivHomeDetailProfileImg.load(R.drawable.ic_sign_up_profile_person)
        binding.root.context.hideKeyboard(binding.root)
        getHomeDetail()
        getHomeFeedDetailData()?.toFeedEntity()?.contentId?.let { homeViewModel.getCommentList(it) }
        statusBarColorOf(R.color.white)
        initBackBtnClickListener()
        initEditText()
        initInputEditTextClickListener()
        initAppbarCancelClickListener()
        initCommentBottomSheet()
        initObserve()
        binding.bottomsheetHomeDetail.tvCommentProfileNickname.text =
            homeViewModel.getUserNickname()
    }

    private fun getHomeDetail() {
        if ((requireArguments().getInt(KEY_NOTI_DATA)) > 0) {
            homeViewModel.getFeedDetail(requireArguments().getInt(KEY_NOTI_DATA))
            homeViewModel.getCommentList(requireArguments().getInt(KEY_NOTI_DATA))
        } else {
            initHomeDetailFeedAdapter()
            getHomeFeedDetailData()?.toFeedEntity()?.contentId?.let {
                homeViewModel.getCommentList(
                    it,
                )
                binding.bottomsheetHomeDetail.etCommentContent.hint =
                    getHomeFeedDetailData()?.toFeedEntity()?.memberNickname + "님에게 답글 남기기"
                binding.bottomsheetHomeDetail.tvCommentFeedUserName.text =
                    homeViewModel.getUserNickname()
                binding.tvHomeDetailInput.text =
                    getHomeFeedDetailData()?.toFeedEntity()?.memberNickname + "님에게 답글 남기기"
                binding.bottomsheetHomeDetail.tvCommentFeedUserName.text =
                    getHomeFeedDetailData()?.toFeedEntity()?.memberNickname
            }
        }
    }

    private fun initHomeDetailFeedAdapter() {
        homeDetailFeedAdapter =
            HomeAdapter(
                onClickKebabBtn = { feedData, positoin ->
                    feedData.contentId?.let {
                        initBottomSheet(
                            feedData.memberId == homeViewModel.getMemberId(),
                            it,
                            false,
                            -1,
                        )
                    }
                },
                onClickTransparentBtn = { data, position ->
                    if (position == -2) {
                        TransparentIsGhostSnackBar.make(binding.root).show()
                    } else {
                        initFeedTransparentDialog(data.memberId, data.contentId ?: -1)
                        updateFeedPosition = position
                    }
                },
                userId = homeViewModel.getMemberId(),
                onClickLikedBtn = { contentId, status ->
                    if (status) {
                        homeViewModel.deleteFeedLiked(contentId)
                    } else {
                        homeViewModel.postFeedLiked(
                            contentId,
                        )
                    }
                },
                onClickUserProfileBtn = { feedData, positoin ->
                    feedData.contentId?.let {
                        navigateToMyPageFragment(feedData.memberId)
                    }
                },
            ).apply {
                submitList(
                    listOf(getHomeFeedDetailData()?.toFeedEntity()),
                )
            }

        contentId = getHomeFeedDetailData()?.contentId ?: -1
    }

    private fun navigateToMyPageFragment(id: Int) {
        findNavController().navigate(
            R.id.action_fragment_home_detail_to_fragment_my_page,
            bundleOf(HomeFragment.KEY_FEED_DATA to id),
        )
    }

    private fun initObserve() {
        homeViewModel.getFeedDetail.flowWithLifecycle(lifecycle).onEach { result ->
            when (result) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    binding.bottomsheetHomeDetail.etCommentContent.hint =
                        result.data.memberNickname + "님에게 답글 남기기"
                    binding.bottomsheetHomeDetail.tvCommentFeedUserName.text =
                        homeViewModel.getUserNickname()
                    binding.tvHomeDetailInput.text = result.data.memberNickname + "님에게 답글 남기기"
                    binding.bottomsheetHomeDetail.tvCommentFeedUserName.text =
                        result.data.memberNickname
                    homeDetailFeedAdapter =
                        HomeAdapter(
                            onClickKebabBtn = { feedData, positoin ->
                                feedData.contentId?.let {
                                    initBottomSheet(
                                        feedData.memberId == homeViewModel.getMemberId(),
                                        it,
                                        false,
                                        -1,
                                    )
                                }
                            },
                            onClickTransparentBtn = { data, position ->
                                if (position == -2) {
                                    TransparentIsGhostSnackBar.make(binding.root).show()
                                } else {
                                    initFeedTransparentDialog(data.memberId, data.contentId ?: -1)
                                    updateFeedPosition = position
                                }
                            },
                            userId = homeViewModel.getMemberId(),
                            onClickLikedBtn = { contentId, status ->
                                if (status) {
                                    homeViewModel.deleteFeedLiked(contentId)
                                } else {
                                    homeViewModel.postFeedLiked(
                                        contentId,
                                    )
                                }
                            },
                        ).apply {
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
                is UiState.Failure -> {
                    requireActivity().startActivity(
                        Intent(
                            requireActivity(),
                            ErrorActivity::class.java,
                        ),
                    )
                    requireActivity().finish()
                }
            }
        }.launchIn(lifecycleScope)

        homeViewModel.getCommentList.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    homeDetailFeedCommentAdapter =
                        HomeDetailCommentAdapter(
                            onClickKebabBtn = { commentData, position ->
                                initBottomSheet(
                                    commentData.memberId == homeViewModel.getMemberId(),
                                    contentId,
                                    true,
                                    commentData.commentId,
                                )
                                deleteCommentPosition = position
                            },
                            onClickLikedBtn = { commentId, status ->
                                if (status) {
                                    homeViewModel.deleteCommentLiked(commentId)
                                } else {
                                    homeViewModel.postCommentLiked(
                                        commentId,
                                    )
                                }
                            },
                            onClickTransparentBtn = { data, position ->
                                if (position == -2) {
                                    TransparentIsGhostSnackBar.make(binding.root).show()
                                } else {
                                    initCommentTransparentDialog(data.memberId, contentId)
                                    updateFeedPosition = position
                                }
                            },
                            userId = homeViewModel.getMemberId(),
                        ).apply {
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
                is UiState.Failure -> {
                    requireActivity().startActivity(
                        Intent(
                            requireActivity(),
                            ErrorActivity::class.java,
                        ),
                    )
                    requireActivity().finish()
                }
            }
        }.launchIn(lifecycleScope)

        homeViewModel.postCommentPosting.observe(
            this,
            EventObserver {
                handleCommentPostingSuccess()
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

        homeViewModel.deleteFeed.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> findNavController().navigateUp()
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)

        homeViewModel.postTransparent.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    homeViewModel.getFeedDetail(contentId)
                    homeViewModel.getCommentList(contentId)
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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        getHomeDetail()
        UploadingSnackBar.make(binding.root).show()
    }

    private fun initBottomSheet(
        isMember: Boolean,
        contentId: Int,
        isComment: Boolean,
        commentId: Int,
    ) {
        HomeBottomSheet(isMember, contentId, isComment, commentId).show(
            parentFragmentManager,
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
            homeViewModel.getFeedDetail(requireArguments().getInt(KEY_NOTI_DATA))
            homeViewModel.getCommentList(requireArguments().getInt(KEY_NOTI_DATA))

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
                val animateProgressBar =
                    AnimateProgressBar(
                        bottomsheetHomeDetail.layoutUploadBar.pbUploadBarInput,
                        0f,
                        bottomsheetHomeDetail.etCommentContent.text.toString().length.toFloat(),
                    )

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
                bottomsheetHomeDetail.layoutUploadBar.pbUploadBarInput.startAnimation(
                    animateProgressBar,
                )
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

    private fun initFeedTransparentDialog(
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) {
        val dialog = TransparentDialogFragment(targetMemberId, alarmTriggerId)
        dialog.show(childFragmentManager, HomeFragment.HOME_TRANSPARENT_DIALOG)
    }

    private fun initCommentTransparentDialog(
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) {
        val dialog = TransparentDialogFragment(targetMemberId, alarmTriggerId)
        dialog.show(childFragmentManager, HomeFragment.HOME_TRANSPARENT_DIALOG)
    }

    companion object {
        const val HOME_DETAIL_BOTTOM_SHEET = "home_detail_bottom_sheet"
        const val KEY_FEED_DATA = "key_feed_data"
    }
}
