package com.teamdontbe.feature.mypage.bottomsheet

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteWithTitleDialogBinding
import com.teamdontbe.feature.mypage.MyPageViewModel
import com.teamdontbe.feature.mypage.comment.MyPageCommentFragment.Companion.FROM_COMMENT
import com.teamdontbe.feature.mypage.feed.MyPageFeedFragment.Companion.FROM_FEED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageDeleteDialogFragment(
    isMember: Boolean,
    contentId: Int,
    commentId: Int,
    whereFrom: String,
) : BindingDialogFragment<FragmentDeleteWithTitleDialogBinding>(R.layout.fragment_delete_with_title_dialog) {
    private val myPageViewModel by activityViewModels<MyPageViewModel>()

    val contentId = contentId ?: -1
    val isMember = isMember ?: false
    val commentId = commentId ?: -1
    val whereFrom = whereFrom ?: "feed"

    override fun initView() {
        initText()
        initCancelButtonClickListener()
        initDeclareBtnClickListener()
    }

    private fun initText() {
        if (isMember) setMemberText() else setNonMemberText()
    }

    private fun setMemberText() {
        val deleteDialogTitleResId =
            when (whereFrom) {
                FROM_FEED -> R.string.tv_delete_with_title_delete_dialog
                else -> R.string.tv_delete_with_title_delete_comment_dialog
            }
        val deleteDialogContentResId =
            when (whereFrom) {
                FROM_FEED -> R.string.tv_delete_with_title_delete_content_dialog
                else -> R.string.tv_delete_with_title_delete_comment_content_dialog
            }

        with(binding) {
            tvDeleteWithTitleDialog.text = getString(deleteDialogTitleResId)
            tvDeleteWithTitleDialogContent.text = getString(deleteDialogContentResId)
            btnDeleteWithTitleDialogDelete.text = getString(R.string.tv_delete_title)
        }
    }

    private fun setNonMemberText() = with(binding) {
        tvDeleteWithTitleDialog.text = getString(R.string.tv_complaint_title)
        tvDeleteWithTitleDialogContent.text =
            getString(R.string.tv_delete_with_title_dialog_content)
        btnDeleteWithTitleDialogDelete.text = getString(R.string.tv_complaint_title)
    }

    private fun initCancelButtonClickListener() {
        binding.btnDeleteWithTitleDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initDeclareBtnClickListener() {
        binding.btnDeleteWithTitleDialogDelete.setOnClickListener {
            if (isMember) deleteFeedOrComment() else navigateToComplaintWeb()
            dismiss()
        }
    }

    private fun deleteFeedOrComment() {
        when (whereFrom) {
            FROM_FEED -> myPageViewModel.deleteFeed(contentId)
            FROM_COMMENT -> myPageViewModel.deleteComment(commentId)
        }
    }

    private fun navigateToComplaintWeb() {
        val urlIntentComplaint =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdjhidNLgk_99uHZ24pCIZX5V0Tn0CQ2sqpW4Aqahr3azQYyA/viewform"),
            )
        startActivity(urlIntentComplaint)
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }
}
