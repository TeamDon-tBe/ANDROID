package com.teamdontbe.feature.dialog

import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentTransparentDialogBinding

class HomeTransparentDialogFragment() :
    BindingDialogFragment<FragmentTransparentDialogBinding>(R.layout.fragment_transparent_dialog) {
    override fun initView() {
        initCancelButtonClickListener()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun initCancelButtonClickListener() {
        binding.btnTransparentDialogCancel.setOnClickListener {
            dismiss()
        }
    }
}
