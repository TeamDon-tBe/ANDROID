package com.teamdontbe.feature.mypage.bottomsheet

import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentTransparentDialogBinding
import com.teamdontbe.feature.mypage.MyPageViewModel

class MyPageTransparentDialogFragment(
    private val alarmTriggerType: String,
    private val targetMemberId: Int,
    private val alarmTriggerId: Int,
) :
    BindingDialogFragment<FragmentTransparentDialogBinding>(R.layout.fragment_transparent_dialog) {
    private val myPageViewModel by activityViewModels<MyPageViewModel>()

    override fun initView() {
        initMyPageYesButtonClickListener()
        initCancelButtonClickListener()
        initMyPageYesButtonClickListener()
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

    private fun initMyPageYesButtonClickListener() {
        binding.btnTransparentDialogYes.setOnClickListener {
            myPageViewModel.postTransparent(alarmTriggerType, targetMemberId, alarmTriggerId)
            dismiss()
        }
    }
}
