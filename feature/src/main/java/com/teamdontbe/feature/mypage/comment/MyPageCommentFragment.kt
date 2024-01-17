package com.teamdontbe.feature.mypage.comment

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.MyPageCommentEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentMyPageCommentBinding
import com.teamdontbe.feature.mypage.MyPageModel
import com.teamdontbe.feature.notification.NotificationFragment.Companion.KEY_NOTI_DATA
import com.teamdontbe.feature.util.FeedItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyPageCommentFragment(id: MyPageModel) :
    BindingFragment<FragmentMyPageCommentBinding>(R.layout.fragment_my_page_comment) {
    private val mockDataViewModel by viewModels<MyPageCommentViewModel>()
    private val memberId = id

    override fun initView() {
        initFeedObserve(memberId.id)
    }

    private fun initFeedObserve(memberId: Int) {
        mockDataViewModel.getMyPageCommentList(memberId)
        mockDataViewModel.getMyPageCommentListState.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> handleSuccessState(it.data)
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun handleSuccessState(feedList: List<MyPageCommentEntity>) {
        if (feedList.isEmpty()) {
            updateNoCommentUI()
        } else {
            initCommentRecyclerView(feedList)
        }
    }

    private fun updateNoCommentUI() = with(binding) {
        rvMyPageComment.visibility = View.GONE
        tvMyPageCommentNoData.visibility = View.VISIBLE
    }

    private fun initCommentRecyclerView(commentData: List<MyPageCommentEntity>) {
        val myPageCommentAdapter = MyPageCommentAdapter(
            onClickKebabBtn = { commentData ->
                // Kebab 버튼 클릭 이벤트 처리
            },
            onItemClicked = { commentData ->
                // RecyclerView 항목 클릭 이벤트 처리
                navigateToHomeDetailFragment(
                    commentData.contentId,
                )
            },
            context = requireContext(),
        ).apply {
            submitList(commentData)
        }

        binding.rvMyPageComment.apply {
            adapter = myPageCommentAdapter
            addItemDecoration(FeedItemDecorator(requireContext()))
        }
    }

    private fun navigateToHomeDetailFragment(id: Int) {
        findNavController().navigate(
            R.id.action_fragment_my_page_to_fragment_home_detail,
            bundleOf(KEY_NOTI_DATA to id),
        )
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}
