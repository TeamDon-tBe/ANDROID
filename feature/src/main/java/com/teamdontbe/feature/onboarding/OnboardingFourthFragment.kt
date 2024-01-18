package com.teamdontbe.feature.onboarding

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.core_ui.util.fragment.drawableOf
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemOnboardingFourthBinding
import com.teamdontbe.feature.posting.PostingViewModel
import com.teamdontbe.feature.signup.SignUpAgreeActivity
import com.teamdontbe.feature.signup.SignUpProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnboardingFourthFragment() :
    BindingFragment<ItemOnboardingFourthBinding>(R.layout.item_onboarding_fourth) {
    private val onboardingViewModel by activityViewModels<OnboardingViewModel>()
    private val signUpProfileViewModel by viewModels<SignUpProfileViewModel>()

    override fun initView() {
        binding.vm = onboardingViewModel
        Timber.d("온보딩 네번째")
        binding.tvOnboardingFourthNickname.text = "돈비 사랑해"
        binding.ivOnboardingFourthProfile.setImageDrawable(drawableOf(R.drawable.img_posting_profile))
        hideKeyboard()

        signUpProfileViewModel.patchUserProfileEdit(
            inputNickName,
            intent.getBooleanExtra(SignUpAgreeActivity.SIGN_UP_AGREE, false),
            binding.etOnboardingFourthIntroduction.text.toString(),
            "",
        )
    }

    private fun hideKeyboard() {
        binding.root.setOnClickListener {
            requireContext().hideKeyboard(binding.root)
        }
    }
}
