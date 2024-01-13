package com.teamdontbe.feature.mypage.transperencyinfo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TransparencyInfoVpAdapter(fr: Fragment) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int = TOTAL_PAGE_NUM

    override fun createFragment(position: Int): Fragment { // 포지션에 따라 어떤 프레그먼트를 보여줄것인지
        return when (position) {
            0 -> TransparencyInfoFirstFragment()
            1 -> TransparencyInfoSecondFragment()
            2 -> TransparencyInfoThirdFragment()
            3 -> TransparencyInfoFourthFragment()
            4 -> TransparencyInfoFifthFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    companion object {
        const val TOTAL_PAGE_NUM = 5
    }
}
