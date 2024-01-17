package com.teamdontbe.feature.home

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomsheetComplaintDeleteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBottomSheet(
    private val isMember: Boolean,
    private val contentId: Int,
    private val isComment: Boolean,
) :
    BindingBottomSheetFragment<BottomsheetComplaintDeleteBinding>(R.layout.bottomsheet_complaint_delete) {
    private val homeViewModel by activityViewModels<HomeViewModel>()

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
            homeViewModel.openComplaintDialog(contentId)
            dismiss()
        }
    }

    private fun initBottomSheetDeleteClickListener() {
        binding.tvDeleteTitle.setOnClickListener {
            if (isComment) {
                //  homeViewModel.openCommentDeleteDialog(contentId)
            } else {
                homeViewModel.openDeleteDialog(
                    contentId,
                )
            }
            dismiss()
        }
    }
}
