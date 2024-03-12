package com.teamdontbe.feature.mypage.comment

import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.fragment.setScrollTopOnReselect
import com.teamdontbe.core_ui.util.fragment.viewLifeCycle
import com.teamdontbe.core_ui.util.fragment.viewLifeCycleScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.CommentEntity
import com.teamdontbe.feature.ErrorActivity.Companion.navigateToErrorPage
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageCommentBinding
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.home.HomeFragment
import com.teamdontbe.feature.homedetail.HomeDetailFragment.Companion.ALARM_TRIGGER_TYPE_COMMENT
import com.teamdontbe.feature.mypage.MyPageFragment.Companion.MY_PAGE_BOTTOM_SHEET
import com.teamdontbe.feature.mypage.MyPageModel
import com.teamdontbe.feature.mypage.MyPageViewModel
import com.teamdontbe.feature.mypage.bottomsheet.MyPageAnotherUserBottomSheet
import com.teamdontbe.feature.mypage.bottomsheet.MyPageTransparentDialogFragment
import com.teamdontbe.feature.mypage.feed.MyPageFeedFragment.Companion.ARG_MEMBER_PROFILE
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
class MyPageCommentFragment :
    BindingFragment<FragmentMyPageCommentBinding>(R.layout.fragment_my_page_comment) {
    private val myPageCommentViewModel by activityViewModels<MyPageViewModel>()

    private lateinit var memberProfile: MyPageModel
    private var deleteCommentPosition: Int = -1

    private var _myPageCommentAdapter: MyPageCommentAdapter? = null
    private val myPageCommentAdapter: MyPageCommentAdapter
        get() = requireNotNull(_myPageCommentAdapter)

    private var deletedItemCount: Int = 0

    override fun initView() {
        initMemberProfile()
        initCommentRecyclerView()
        initDeleteObserve()
        initTransparentObserve(memberProfile.id)
        stateCommentItemNull()
        //scrollRecyclerViewToTop()
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

    private fun initCommentRecyclerView() {
        _myPageCommentAdapter =
            MyPageCommentAdapter(
                context = requireContext(),
                idFlag = memberProfile.idFlag,
                onClickKebabBtn = ::clickKebabBtn,
                onItemClicked = { commentData ->
                    navigateToHomeDetailFragment(commentData.contentId ?: -1)
                },
                onClickLikedBtn = ::onLikedBtnClick,
                onClickTransparentBtn = ::onTransparentBtnClick,
            ).apply {
                pagingSubmitData(
                    viewLifecycleOwner,
                    myPageCommentViewModel.getMyPageCommentList(memberProfile.id),
                    pagingAdapter = this
                )
            }

        binding.rvMyPageComment.adapter =
            myPageCommentAdapter.withLoadStateFooter(footer = PagingLoadingAdapter())

        setUpItemDecorator()
    }

    private fun clickKebabBtn(commentEntity: CommentEntity, position: Int) {
        commentEntity.let {
            initBottomSheet(
                it.memberId == myPageCommentViewModel.getMemberId(),
                contentId = it.contentId ?: -1,
                commentId = it.commentId,
                whereFrom = FROM_COMMENT,
            )
            deleteCommentPosition = position
        }
    }

    private fun initBottomSheet(
        isMember: Boolean,
        contentId: Int,
        commentId: Int,
        whereFrom: String,
    ) {
        MyPageAnotherUserBottomSheet(isMember, contentId, commentId, whereFrom).show(
            childFragmentManager,
            MY_PAGE_BOTTOM_SHEET,
        )
    }

    private fun onLikedBtnClick(
        contentId: Int,
        status: Boolean,
    ) {
        if (status) {
            myPageCommentViewModel.deleteCommentLiked(contentId)
        } else {
            myPageCommentViewModel.postCommentLiked(contentId)
        }
    }

    private fun onTransparentBtnClick(data: CommentEntity) {
        if (data.isGhost) {
            TransparentIsGhostSnackBar.make(binding.root).show()
        } else {
            initTransparentDialog(data.memberId, data.commentId ?: -1)
        }
    }

    private fun setUpItemDecorator() {
        if (binding.rvMyPageComment.itemDecorationCount == 0) {
            binding.rvMyPageComment.addItemDecoration(
                FeedItemDecorator(requireContext()),
            )
        }
    }

    private fun initDeleteObserve() {
        myPageCommentViewModel.deleteComment.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> handleDeleteCommentSuccess()
                is UiState.Failure -> navigateToErrorPage(requireContext())
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun handleDeleteCommentSuccess() {
        if (deleteCommentPosition != -1) {
            myPageCommentAdapter.deleteItem(deleteCommentPosition)
            deletedItemCount++
            updateUiBasedOnItemCount()
            deleteCommentPosition = -1
            showDeleteCompleteDialog()
        }
    }

    private fun updateUiBasedOnItemCount() {
        if (myPageCommentAdapter.itemCount == deletedItemCount) updateNoCommentUI() else updateExistCommentUI()
    }

    private fun showDeleteCompleteDialog() {
        val dialog = DeleteCompleteDialogFragment()
        dialog.show(childFragmentManager, KeyStorage.DELETE_POSTING)
    }

    private fun stateCommentItemNull() {
        myPageCommentAdapter.addLoadStateListener { combinedLoadStates ->
            val isEmpty = combinedLoadStates.source.refresh is
            LoadState.NotLoading && combinedLoadStates.append.endOfPaginationReached && myPageCommentAdapter.itemCount < 1
            if (isEmpty) {
                updateNoCommentUI()
            } else {
                updateExistCommentUI()
            }
        }
    }

    private fun updateNoCommentUI() = with(binding) {
        rvMyPageComment.isGone = true
        tvMyPageCommentNoData.apply {
            isVisible = true
            text = if (memberProfile.idFlag) getString(R.string.my_page_comment_my_not_yet)
            else getString(
                R.string.my_page_comment_other_not_yet,
                memberProfile.nickName
            )
        }
    }

    private fun updateExistCommentUI() = with(binding) {
        rvMyPageComment.isVisible = true
        tvMyPageCommentNoData.isGone = true
    }

    private fun initTransparentObserve(memberId: Int) {
        myPageCommentViewModel.postTransparent.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> myPageCommentViewModel.getMyPageCommentList(memberId)
                is UiState.Failure -> navigateToErrorPage(requireContext())
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initTransparentDialog(
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) {
        val dialog = MyPageTransparentDialogFragment(
            ALARM_TRIGGER_TYPE_COMMENT,
            targetMemberId,
            alarmTriggerId
        )
        dialog.show(childFragmentManager, HomeFragment.HOME_TRANSPARENT_DIALOG)
    }

    private fun navigateToHomeDetailFragment(id: Int) {
        findNavController().navigate(
            R.id.action_fragment_my_page_to_fragment_home_detail,
            bundleOf(KEY_NOTI_DATA to id),
        )
    }

    private fun scrollRecyclerViewToTop() {
        // (reselect하는 icon, 바텀네비 ,scroll할 리사이클러뷰)
        setScrollTopOnReselect(
            R.id.fragment_my_page,
            R.id.bnv_main,
            binding.rvMyPageComment
        )
    }

    companion object {
        const val FROM_COMMENT = "comment"
        fun newInstance(memberProfile: MyPageModel?): MyPageCommentFragment {
            return MyPageCommentFragment().apply {
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
        _myPageCommentAdapter = null
    }
}
