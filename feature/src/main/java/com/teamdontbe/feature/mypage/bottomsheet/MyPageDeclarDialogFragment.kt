package com.teamdontbe.feature.mypage.bottomsheet

import android.content.Intent
import android.net.Uri
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteWithTitleDialogBinding

class MyPageDeclarDialogFragment :
    BindingDialogFragment<FragmentDeleteWithTitleDialogBinding>(R.layout.fragment_delete_with_title_dialog) {
    override fun initView() {
        initText()
        initCancelButtonClickListener()
        initDeclareBtnClickListener()
    }

    private fun initText() {
        binding.tvDeleteWithTitleDialogContent.text =
            getString(R.string.tv_delete_with_title_dialog_content)
        binding.btnDeleteWithTitleDialogDelete.text = getString(R.string.tv_complaint_title)
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
            navigateToComplaintWeb()
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
