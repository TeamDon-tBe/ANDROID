package com.teamdontbe.feature.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomePostingRestrictionDialogBinding

class HomePostingRestrictionDialogFragment() :
    BindingDialogFragment<FragmentHomePostingRestrictionDialogBinding>(R.layout.fragment_home_posting_restriction_dialog) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        initCheckButtonClick()
    }

    override fun onResume() {
        context?.dialogFragmentResize(this, 25.0f)
//        binding.layoutHomePostingRestriction.isVisible = true
        super.onResume()
    }

    private fun initCheckButtonClick() {
        binding.btnHomePostingRestrictionDialogCheck.setOnClickListener {
            navigateToMainActivity()
            dismiss()
        }
    }

    private fun navigateToMainActivity() {
        findNavController().navigate(
            R.id.action_posting_to_home,
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}
