package com.teamdontbe.feature.mypage.bottomsheet

import android.content.Intent
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomSheetMyPageHambergerBinding
import com.teamdontbe.feature.mypage.MyPageAuthInfoActivity
import com.teamdontbe.feature.signup.SignUpProfileActivity

class MyPageBottomSheet :
    BindingBottomSheetFragment<BottomSheetMyPageHambergerBinding>(R.layout.bottom_sheet_my_page_hamberger) {
    override fun initView() {
        initBottomSheetCloseClickListener()
        navigateToSignUpProfileActivity()
        navigateToMyPageAuthInfoFragment()
    }

    private fun initBottomSheetCloseClickListener() {
        binding.ivMyPageBottomSheetClose.setOnClickListener {
            dismiss()
        }
    }

    private fun navigateToSignUpProfileActivity() {
        binding.tvMyPageBottomSheetEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), SignUpProfileActivity::class.java))
        }
    }

    private fun navigateToMyPageAuthInfoFragment() {
        binding.tvMyPageBottomSheetAccountInfo.setOnClickListener {
            startActivity(Intent(requireContext(), MyPageAuthInfoActivity::class.java))
        }
    }
}
