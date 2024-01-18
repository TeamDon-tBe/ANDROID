package com.teamdontbe.feature.mypage.bottomsheet

import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteWithTitleDialogBinding
import com.teamdontbe.feature.mypage.feed.MyPageFeedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageDeleteDialogFragment(
    isMember: Boolean,
    contentId: Int,
    isComment: Boolean,
    commentId: Any?,
) : BindingDialogFragment<FragmentDeleteWithTitleDialogBinding>(R.layout.fragment_delete_with_title_dialog) {
    private val myPageFeedViewModel by activityViewModels<MyPageFeedViewModel>()
    val contentId = contentId ?: -1

    override fun initView() {
        initText()
        initCancelButtonClickListener()
        initDeclareBtnClickListener()
    }

    private fun initText() {
        binding.tvDeleteWithTitleDialogContent.text =
            getString(R.string.tv_delete_with_title_delete_dialog)
        binding.btnDeleteWithTitleDialogDelete.text = getString(R.string.tv_delete_title)
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun initCancelButtonClickListener() {
        binding.btnDeleteWithTitleDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initDeclareBtnClickListener() {
        binding.btnDeleteWithTitleDialogDelete.setOnClickListener {
            myPageFeedViewModel.deleteFeed(contentId)
            dismiss()
        }
    }
}
