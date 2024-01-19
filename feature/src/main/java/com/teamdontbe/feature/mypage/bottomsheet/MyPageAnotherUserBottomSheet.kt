package com.teamdontbe.feature.mypage.bottomsheet

import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetComplaintDeleteBinding

class MyPageAnotherUserBottomSheet(
    private val isMember: Boolean,
    private val contentId: Int,
    private val commentId: Int,
    private val whereFrom: String,
) :
    BindingBottomSheetFragment<BottomsheetComplaintDeleteBinding>(R.layout.bottomsheet_complaint_delete) {
    override fun initView() {
        initBottomSheetCloseClickListener()
        initShowDialogClickListener()
        initText()
    }

    private fun initText() {
        if (isMember) {
            binding.tvComplaintTitle.text = getString(R.string.tv_delete_title)
        }
    }

    private fun initBottomSheetCloseClickListener() {
        binding.ivComplaintDeleteClose.setOnClickListener {
            dismiss()
        }
    }

    private fun initShowDialogClickListener() {
        binding.tvComplaintTitle.setOnClickListener {
            MyPageDeleteDialogFragment(
                isMember,
                contentId,
                commentId,
                whereFrom,
            ).show(
                parentFragmentManager,
                DECLARE_DIALOG,
            )
            dismiss()
        }
    }

    companion object {
        const val DECLARE_DIALOG = "declare"
    }
}
