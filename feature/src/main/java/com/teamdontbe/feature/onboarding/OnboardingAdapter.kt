package com.teamdontbe.feature.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(
    fragmentFragment: OnboardingFragment,
    private val click: (String) -> Unit = { _ -> },
) :
    FragmentStateAdapter(fragmentFragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> OnboardingSecondItemFragment()
            2 -> OnboardingThirdFragment()
            3 -> OnboardingFourthFragment(click)
            else -> OnboardingFirstItemFragment()
        }
    }
}
