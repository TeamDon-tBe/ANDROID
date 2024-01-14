package com.teamdontbe.feature.mypage.bottomsheet

import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomSheetMyPageHambergerBinding

class MyPageBottomSheet :
    BindingBottomSheetFragment<BottomSheetMyPageHambergerBinding>(R.layout.bottom_sheet_my_page_hamberger) {
    override fun initView() {
        initBottomSheetCloseClickListener()
        navigateToMyPageAuthInfoFragment()
        navigateToSignUpProfileActivity()
    }

    private fun initBottomSheetCloseClickListener() {
        binding.ivComplaintDeleteClose.setOnClickListener {
            dismiss()
        }
    }

    private fun navigateToMyPageAuthInfoFragment() {
        binding.tvDeleteTitle.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_my_page_to_myPageAuthInfoFragment,
            )
            dismiss()
        }
    }

    private fun navigateToSignUpProfileActivity() {
        binding.tvComplaintTitle.setOnClickListener {
            findNavController().navigate(
                R.id.action_fragment_my_page_to_signUpProfileActivity,
            )
            dismiss()
        }
    }
}
