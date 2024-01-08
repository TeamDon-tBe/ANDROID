package com.teamdontbe.feature.onboarding

import android.view.View
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentOnboardingBinding

class OnboardingFragment :
    BindingFragment<FragmentOnboardingBinding>(R.layout.fragment_onboarding) {
    private var _onboardingAdapter: OnboardingAdapter? = null
    private val onboardingAdapter
        get() = requireNotNull(_onboardingAdapter) { "adapter 초기화 안됨" }

    override fun initView() {
        initOnboardingAdapter()
    }

    private val pageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.btnOnboardingStart.isVisible = position == 3
                binding.btnOnboardingNext.isVisible = position != 3
                binding.tvOnboardingSkip.isVisible = position != 3
                binding.ivOnboardingBack.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
            }
        }

    private fun initOnboardingAdapter() {
        _onboardingAdapter = OnboardingAdapter(this)
        binding.vpOnboarding.adapter = _onboardingAdapter
        binding.vpOnboarding.isUserInputEnabled = false
        binding.vpOnboarding.registerOnPageChangeCallback(pageChangeCallback)
        binding.btnOnboardingNext.setOnClickListener {
            if (binding.vpOnboarding.currentItem + 1 < (_onboardingAdapter?.itemCount ?: 0)) {
                binding.vpOnboarding.setCurrentItem(binding.vpOnboarding.currentItem + 1, true)
            }
        }
        binding.ivOnboardingBack.setOnClickListener {
            if (-1 < binding.vpOnboarding.currentItem - 1) {
                binding.vpOnboarding.setCurrentItem(binding.vpOnboarding.currentItem - 1, true)
            }
        }
        TabLayoutMediator(binding.tabOnboardingIndicator, binding.vpOnboarding) { _, _ ->
        }.attach()
    }

    override fun onDestroyView() {
        _onboardingAdapter = null
        super.onDestroyView()
    }
}
