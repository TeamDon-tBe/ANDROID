package com.teamdontbe.feature.dialog

import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentTransparentDialogBinding
import com.teamdontbe.feature.home.HomeViewModel
import com.teamdontbe.feature.mypage.MyPageViewModel

class TransparentDialogFragment(
    private val targetMemberId: Int,
    private val alarmTriggerId: Int,
) :
    BindingDialogFragment<FragmentTransparentDialogBinding>(R.layout.fragment_transparent_dialog) {
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val myPageViewModel by activityViewModels<MyPageViewModel>()

    override fun initView() {
        initYesButtonClickListener()
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

    private fun initYesButtonClickListener() {
        binding.btnTransparentDialogYes.setOnClickListener {
            homeViewModel.postTransparent(targetMemberId, alarmTriggerId)
            dismiss()
        }
    }

    private fun initMyPageYesButtonClickListener() {
        binding.btnTransparentDialogYes.setOnClickListener {
            myPageViewModel.postTransparent(targetMemberId, alarmTriggerId)
            dismiss()
        }
    }
}
