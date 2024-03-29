package com.teamdontbe.feature.onboarding

import androidx.fragment.app.activityViewModels
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
        hideKeyboard()
    }

    private fun hideKeyboard() {
        binding.root.setOnClickListener {
            requireContext().hideKeyboard(binding.root)
        }
    }
}
