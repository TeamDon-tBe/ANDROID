package com.teamdontbe.feature.dialog

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteWithTitleDialogBinding
import com.teamdontbe.feature.home.HomeBottomSheet
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.homedetail.HomeDetailFragment
import com.teamdontbe.feature.homedetail.HomeDetailFragment.Companion.HOME_DETAIL_BOTTOM_SHEET
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteWithTitleDialogFragment(
    val title: String,
    val content: String,
    private val isMember: Boolean,
    private val contentId: Int,
    private val isComment: Boolean,
    private val commentId: Int,
) : BindingDialogFragment<FragmentDeleteWithTitleDialogBinding>(R.layout.fragment_delete_with_title_dialog) {
    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun initView() {
        initText()
        initCancelButtonClickListener()
        initDeleteButtonClickListener()
    }

    private fun initText() {
        binding.tvDeleteWithTitleDialog.text = title
        binding.tvDeleteWithTitleDialogContent.text = content
        if (isMember) {
            binding.btnDeleteWithTitleDialogDelete.text = getString(R.string.tv_complaint_title)
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 24.0f)
    }

    private fun initCancelButtonClickListener() {
        binding.btnDeleteWithTitleDialogCancel.setOnClickListener {
            dismiss()
            (parentFragmentManager.findFragmentByTag(HomeDetailFragment.HOME_DETAIL_BOTTOM_SHEET) as? HomeBottomSheet)?.dismiss()
        }
    }

    private fun initDeleteButtonClickListener() {
        binding.btnDeleteWithTitleDialogDelete.setOnClickListener {
            if (!isMember && !isComment) {
                homeViewModel.deleteFeed(contentId)
            } else if (!isMember && isComment) {
                homeViewModel.deleteComment(commentId)
            } else {
                navigateToComplaintWeb()
            }
            dismiss()
            (parentFragmentManager.findFragmentByTag(HomeDetailFragment.HOME_DETAIL_BOTTOM_SHEET) as? HomeBottomSheet)?.dismiss()
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
}
