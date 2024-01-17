package com.teamdontbe.feature.mypage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamdontbe.feature.mypage.comment.MyPageCommentFragment
import com.teamdontbe.feature.mypage.feed.MyPageFeedFragment

class MyPageVpAdapter(fr: Fragment, data: MyPageModel) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int = TOTAL_TAG_NUM
    private val memberProfile = data

    override fun createFragment(position: Int): Fragment { // 포지션에 따라 어떤 프레그먼트를 보여줄것인지
        return when (position) {
            0 -> MyPageFeedFragment(memberProfile)
            1 -> MyPageCommentFragment(memberProfile)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    companion object {
        const val TOTAL_TAG_NUM = 2
    }
}
