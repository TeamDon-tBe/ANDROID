package com.teamdontbe.feature.onboarding

import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ItemOnboardingFirstBinding
import timber.log.Timber

class OnboardingFirstItemFragment :
    BindingFragment<ItemOnboardingFirstBinding>(R.layout.item_onboarding_first) {
    override fun initView() {
        Timber.d("온보딩 첫번째")
    }
}
