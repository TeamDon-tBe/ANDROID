package com.teamdontbe.feature.mypage.bottomsheet

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import com.teamdontbe.core_ui.base.BindingBottomSheetFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.BottomSheetMyPageHambergerBinding
import com.teamdontbe.feature.dialog.DeleteWithTitleWideDialogFragment
import com.teamdontbe.feature.mypage.MyPageFragment
import com.teamdontbe.feature.mypage.authinfo.MyPageAuthInfoActivity
import com.teamdontbe.feature.mypage.authinfo.MyPagePushAlarmActivity
import com.teamdontbe.feature.signup.SignUpProfileActivity
import com.teamdontbe.feature.util.DialogTag.LOGOUT_AUTH

class MyPageBottomSheet :
    BindingBottomSheetFragment<BottomSheetMyPageHambergerBinding>(R.layout.bottom_sheet_my_page_hamberger) {
    override fun initView() {
        initBottomSheetCloseClickListener()
        navigateToSignUpProfileActivity()
        navigateToMyPageAuthInfoFragment()
        initCustomerCenterClickListener()
        initLogoutClickListener()
        navigateToMyPagePushAlarmActivity()
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

    private fun initCustomerCenterClickListener() {
        binding.tvMyPageBottomSheetCustomService.setOnClickListener {
            navigateToComplaintWeb("https://joyous-ghost-8c7.notion.site/Don-t-be-e949f7751de94ba682f4bd6792cbe36e")
        }
        binding.tvMyPageBottomSheetFeedback.setOnClickListener {
            navigateToComplaintWeb("https://forms.gle/DqnypURRBDks7WqJ6")
        }
    }

    private fun navigateToComplaintWeb(uri: String) {
        val urlIntentComplaint =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri),
            )
        startActivity(urlIntentComplaint)
    }

    private fun initLogoutClickListener() {
        binding.tvMyPageBottomSheetLogout.setOnClickListener {
            val dialog =
                DeleteWithTitleWideDialogFragment(
                    getString(R.string.bottom_sheet_my_page_logout),
                    getString(R.string.bottom_sheet_my_page_logout_description),
                    true,
                    "",
                )
            dialog.show(childFragmentManager, LOGOUT_AUTH)
        }
    }

    private fun navigateToMyPagePushAlarmActivity() {
        binding.tvMyPageBottomSheetPushAlarm.setOnClickListener {
            Intent(requireContext(), MyPagePushAlarmActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as MyPageFragment).updateUI() // AFrag은 A Fragment의 클래스 이름입니다.
    }

    companion object {
        const val MY_PAGE_PROFILE = "myPageProfile"
        private const val APP_BAR_TITLE = "프로필 화면"
    }
}
