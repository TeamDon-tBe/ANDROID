package com.teamdontbe.feature.dialog

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteWithTitleDialogBinding
import com.teamdontbe.feature.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteWithTitleDialogFragment(
    val title: String,
    val content: String,
    private val isMember: Boolean,
    private val contentId: Int,
    private val isContent: Boolean,
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
        if (!isMember) {
            binding.btnDeleteWithTitleDialogDelete.text =
                getString(R.string.tv_complaint_title)
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 24.0f)
    }

    private fun initCancelButtonClickListener() {
        binding.btnDeleteWithTitleDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initDeleteButtonClickListener() {
        binding.btnDeleteWithTitleDialogDelete.setOnClickListener {
            if (isMember && !isContent) {
                homeViewModel.deleteFeed(contentId)
            } else if (isMember && isContent) {
                homeViewModel.deleteComment(contentId)
            } else {
                navigateToComplaintWeb()
            }
            dismiss()
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
