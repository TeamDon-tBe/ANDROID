package com.teamdontbe.feature.dialog

import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentTransparentReasonDialogBinding
import com.teamdontbe.feature.home.HomeViewModel

class TransparentReasonDialogFragment(
    private val alarmTriggerType: String,
    private val targetMemberId: Int,
    private val alarmTriggerId: Int,
) : BindingDialogFragment<FragmentTransparentReasonDialogBinding>(R.layout.fragment_transparent_reason_dialog) {
    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun initView() {
        initYesButtonClickListener()
        initCancelButtonClickListener()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun initCancelButtonClickListener() {
        binding.btnTransparentReasonCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initYesButtonClickListener() {
        binding.btnTransparentReasonYes.setOnClickListener {
            homeViewModel.postTransparent(alarmTriggerType, targetMemberId, alarmTriggerId)
            dismiss()
        }
    }
}