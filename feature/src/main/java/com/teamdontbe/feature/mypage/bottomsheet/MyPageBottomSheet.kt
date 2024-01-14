package com.teamdontbe.feature.mypage.bottomsheet

import android.content.Intent
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomSheetMyPageHambergerBinding
import com.teamdontbe.feature.login.SignUpProfileActivity
import com.teamdontbe.feature.mypage.MyPageAuthInfoActivity

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
            val intent = Intent(requireContext(), SignUpProfileActivity::class.java)
            intent.putExtra(MY_PAGE_PROFILE, APP_BAR_TITLE)
            startActivity(intent)
        }
    }

    private fun navigateToMyPageAuthInfoFragment() {
        binding.tvMyPageBottomSheetAccountInfo.setOnClickListener {
            startActivity(Intent(requireContext(), MyPageAuthInfoActivity::class.java))
        }
    }

    companion object {
        const val MY_PAGE_PROFILE = "myPageProfile"
        private const val APP_BAR_TITLE = "프로필 화면"
    }
}
