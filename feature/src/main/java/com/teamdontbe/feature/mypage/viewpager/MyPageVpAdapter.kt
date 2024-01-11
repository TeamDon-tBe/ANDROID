package com.teamdontbe.feature.mypage.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageVpAdapter(fr: Fragment) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int = TOTAL_TAG_NUM

    override fun createFragment(position: Int): Fragment { // 포지션에 따라 어떤 프레그먼트를 보여줄것인지
        return when (position) {
            0 -> MyPageCommentFragment()
            1 -> MyPageCommentFragment()
            else -> MyPageCommentFragment()
        }
    }

    companion object {
        const val TOTAL_TAG_NUM = 2
    }
}
