package com.teamdontbe.feature.mypage.bottomsheet

import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetComplaintDeleteBinding

class MyPageAnotherUserBottomSheet :
    BindingBottomSheetFragment<BottomsheetComplaintDeleteBinding>(R.layout.bottomsheet_complaint_delete) {
    override fun initView() {
        initBottomSheetCloseClickListener()
        initShowDialogClickListener()
    }

    private fun initBottomSheetCloseClickListener() {
        binding.ivComplaintDeleteClose.setOnClickListener {
            dismiss()
        }
    }

    private fun initShowDialogClickListener() {
        binding.tvComplaintTitle.setOnClickListener {
            MyPageDeclarDialogFragment().show(
                childFragmentManager,
                DECLARE_DIALOG,
            )
        }
    }

    companion object {
        const val DECLARE_DIALOG = "declare"
    }
}
