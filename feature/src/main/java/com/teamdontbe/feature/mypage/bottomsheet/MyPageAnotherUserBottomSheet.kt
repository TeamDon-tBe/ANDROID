package com.teamdontbe.feature.mypage.bottomsheet

import android.view.View
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetComplaintDeleteBinding

class MyPageAnotherUserBottomSheet(
    private val isMember: Boolean,
    private val contentId: Int,
    private val commentId: Int,
    private val whereFrom: String,
) : BindingBottomSheetFragment<BottomsheetComplaintDeleteBinding>(R.layout.bottomsheet_complaint_delete) {

    override fun initView() {
        initText()
        initClickListener()
    }

    private fun initText() {
        val complaintTitleVisibility = if (isMember) View.GONE else View.VISIBLE
        val deleteTitleVisibility = if (isMember) View.VISIBLE else View.GONE

        binding.tvComplaintTitle.visibility = complaintTitleVisibility
        binding.tvDeleteTitle.visibility = deleteTitleVisibility
    }

    private fun initClickListener() {
        binding.ivComplaintDeleteClose.setOnClickListener {
            dismiss()
        }

        binding.tvComplaintTitle.setOnClickListener {
            showDialog()
        }

        binding.tvDeleteTitle.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
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

    companion object {
        const val DECLARE_DIALOG = "declare"
    }
}
