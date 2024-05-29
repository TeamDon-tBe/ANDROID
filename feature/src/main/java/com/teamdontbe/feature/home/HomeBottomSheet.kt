package com.teamdontbe.feature.home

import androidx.core.view.isVisible
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetComplaintDeleteBinding
import com.teamdontbe.feature.dialog.DeleteWithTitleDialogFragment
import com.teamdontbe.feature.util.BottomSheetTag.HOME_DETAIL_BOTTOM_SHEET
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBottomSheet(
    private val isMember: Boolean,
    private val contentId: Int,
    private val isComment: Boolean,
    private val commentId: Int,
    private val reportTargetNickname: String,
    private val relateText: String,
) : BindingBottomSheetFragment<BottomsheetComplaintDeleteBinding>(R.layout.bottomsheet_complaint_delete) {
    override fun initView() {
        setBottomSheetType()
        initBottomSheetCloseClickListener()
        initBottomSheetComplaintClickListener()
        initBottomSheetDeleteClickListener()
    }

    private fun setBottomSheetType() {
        binding.tvDeleteTitle.isVisible = isMember
    }

    private fun initBottomSheetCloseClickListener() {
        binding.ivComplaintDeleteClose.setOnClickListener {
            dismiss()
        }
    }

    private fun initBottomSheetComplaintClickListener() {
        binding.tvComplaintTitle.setOnClickListener {
            if (isComment) {
                DeleteWithTitleDialogFragment(
                    getString(R.string.tv_delete_with_title_complain_dialog),
                    getString(R.string.tv_delete_with_title_dialog_comment),
                    true,
                    contentId,
                    true,
                    commentId,
                    reportTargetNickname,
                    relateText,
                ).show(parentFragmentManager, HOME_DETAIL_BOTTOM_SHEET)
            } else {
                DeleteWithTitleDialogFragment(
                    getString(R.string.tv_delete_with_title_complain_dialog),
                    getString(R.string.tv_delete_with_title_dialog_content),
                    true,
                    contentId,
                    false,
                    commentId,
                    reportTargetNickname,
                    relateText,
                ).show(parentFragmentManager, HOME_DETAIL_BOTTOM_SHEET)
            }
        }
    }

    private fun initBottomSheetDeleteClickListener() {
        binding.tvDeleteTitle.setOnClickListener {
            if (isComment) {
                DeleteWithTitleDialogFragment(
                    getString(R.string.tv_delete_with_title_delete_comment_dialog),
                    getString(R.string.tv_delete_with_title_delete_comment_content_dialog),
                    false,
                    contentId,
                    true,
                    commentId,
                ).show(parentFragmentManager, HOME_DETAIL_BOTTOM_SHEET)
            } else {
                DeleteWithTitleDialogFragment(
                    getString(R.string.tv_delete_with_title_delete_dialog),
                    getString(R.string.tv_delete_with_title_delete_content_dialog),
                    false,
                    contentId,
                    false,
                    commentId,
                ).show(parentFragmentManager, HOME_DETAIL_BOTTOM_SHEET)
            }
            dismiss()
        }
    }
}
