package com.teamdontbe.feature.dialog

import android.view.View
import androidx.fragment.app.activityViewModels
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.core_ui.util.fragment.toast
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentTransparentDialogBinding
import com.teamdontbe.feature.home.HomeViewModel

class TransparentDialogFragment(
    private val alarmTriggerType: String,
    private val targetMemberId: Int,
    private val alarmTriggerId: Int
) : BindingDialogFragment<FragmentTransparentDialogBinding>(R.layout.fragment_transparent_dialog) {
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
        binding.btnTransparentDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initYesButtonClickListener() {
        binding.btnTransparentDialogYes.setOnClickListener {
            toast(getGhostReason())
            if(getGhostReason().isEmpty()){
                binding.tvTransparentWarning.visibility = View.VISIBLE
            }else{
                binding.tvTransparentWarning.visibility = View.INVISIBLE
//                homeViewModel.postTransparent(
//                    alarmTriggerType, targetMemberId, alarmTriggerId,
//                    getGhostReason()
//                )
//                dismiss()
            }
        }
    }

    private fun getGhostReason(): String {
        with(binding) {
            return when {
                rbTransparentContent1.isChecked -> getString(R.string.rb_transparent_reason_content_1)
                rbTransparentContent1.isChecked -> getString(R.string.rb_transparent_reason_content_2)
                rbTransparentContent1.isChecked -> getString(R.string.rb_transparent_reason_content_3)
                rbTransparentContent1.isChecked -> getString(R.string.rb_transparent_reason_content_4)
                rbTransparentContent1.isChecked -> getString(R.string.rb_transparent_reason_content_5)
                rbTransparentContent1.isChecked -> getString(R.string.my_page_auth_withdraw_reason_content_7)
                else -> ""
            }
        }
    }
}
