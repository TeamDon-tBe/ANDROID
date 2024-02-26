package com.teamdontbe.feature.onboarding

import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import coil.load
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.context.hideKeyboard
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemOnboardingFourthBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnboardingFourthFragment() :
    BindingFragment<ItemOnboardingFourthBinding>(R.layout.item_onboarding_fourth) {
    private val onboardingViewModel by activityViewModels<OnboardingViewModel>()

    override fun initView() {
        binding.vm = onboardingViewModel
        Timber.d("온보딩 네번째")
        binding.tvOnboardingFourthNickname.text = onboardingViewModel.getNickName()
        binding.ivOnboardingFourthProfile.load(onboardingViewModel.getUserProfile())
        hideKeyboard()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun hideKeyboard() {
        binding.root.setOnClickListener {
            requireContext().hideKeyboard(binding.root)
        }
    }

    override fun onDestroyView() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        super.onDestroyView()
    }
}
