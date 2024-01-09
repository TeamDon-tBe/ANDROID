package com.teamdontbe.feature.onboarding

import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.fragment.colorOf
import com.teamdontbe.core_ui.view.setTextColorAsLinearGradient
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemOnboardingThirdBinding
import timber.log.Timber

class OnboardingThirdFragment :
    BindingFragment<ItemOnboardingThirdBinding>(R.layout.item_onboarding_third) {
    override fun initView() {
        Timber.d("온보딩 세번째")
        setTextColorGradient()
    }

    private fun setTextColorGradient() {
        binding.tvOnboardingThirdTitleGradient.setTextColorAsLinearGradient(
            (
                arrayOf(
                    colorOf(R.color.black),
                    colorOf(R.color.white),
                )
            ),
        )
    }
}
