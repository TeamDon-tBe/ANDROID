package com.teamdontbe.feature.mypage.bottomsheet

import android.content.Intent
import android.net.Uri
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomSheetMyPageHambergerBinding
import com.teamdontbe.feature.mypage.authinfo.MyPageAuthInfoActivity
import com.teamdontbe.feature.signup.SignUpProfileActivity

class MyPageBottomSheet :
    BindingBottomSheetFragment<BottomSheetMyPageHambergerBinding>(R.layout.bottom_sheet_my_page_hamberger) {
    override fun initView() {
        initBottomSheetCloseClickListener()
        navigateToSignUpProfileActivity()
        navigateToMyPageAuthInfoFragment()
        initCustomerCenterClickListener()
    }

    private fun initCustomerCenterClickListener() {
        binding.tvMyPageBottomSheetCustomService.setOnClickListener {
            navigateToComplaintWeb()
        }
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

    private fun navigateToComplaintWeb() {
        val urlIntentComplaint =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://joyous-ghost-8c7.notion.site/Don-t-be-e949f7751de94ba682f4bd6792cbe36e"),
            )
        startActivity(urlIntentComplaint)
    }

    companion object {
        const val MY_PAGE_PROFILE = "myPageProfile"
        private const val APP_BAR_TITLE = "프로필 화면"
    }
}
