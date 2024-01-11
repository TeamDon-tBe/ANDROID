package com.teamdontbe.feature.home

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomeBinding
import com.teamdontbe.feature.mypage.adapter.MyPageFeedItemDecorator

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun initView() {
        initHomeAdapter()
    }

    private fun initHomeAdapter() {
        binding.rvHome.adapter =
            HomeAdapter(onClickKebabBtn = { feedData, position ->
                initBottomSheet()
            }, onClickToNavigateToHomeDetail = { feedData, position ->
                navigateToHomeDetailFragment(
                    Feed(
                        feedData.memberId,
                        feedData.memberNickname,
                        feedData.memberNickname,
                        feedData.isLiked,
                        feedData.isGhost,
                        feedData.memberGhost,
                        feedData.contentLikedNumber,
                        feedData.commentNumber,
                        feedData.contentText,
                        feedData.time,
                    ),
                )
            }).apply {
                submitList(
                    listOf(
                        FeedEntity(
                            3, "", "돈비돈비", false, false, 90, 200, 60, "돈비 사랑해", "2시간 전",
                        ),
                        FeedEntity(
                            1, "", "먼지먼징", false, false, 100, 100, 30, "먼지는 성장기", "2시간 전",
                        ),
                        FeedEntity(
                            2, "", "쿼카햄토리", false, false, 80, 50, 10, "쿼카햄은 짜파구리 요리사", "1시간 전",
                        ),
                        FeedEntity(
                            4, "", "보람상쥐", false, false, 100, 100, 30, "진실의 방으로", "2시간 전",
                        ),
                        FeedEntity(
                            5, "", "파주멀당", false, false, 80, 50, 10, "상인", "1시간 전",
                        ),
                        FeedEntity(
                            6, "", "아 예예~", false, false, 90, 200, 60, "yes걸", "2시간 전",
                        ),
                        FeedEntity(
                            6, "", "정언명렁-칸트", false, false, 90, 200, 60, "권나라", "2시간 전",
                        ),
                        FeedEntity(
                            6, "", "희죽희죽", false, false, 90, 200, 60, "희죽", "2시간 전",
                        ),
                        FeedEntity(
                            6, "", "쥬쥬", false, false, 90, 200, 60, "시크릿 공주", "2시간 전",
                        ),
                        FeedEntity(
                            6, "", "상우야 변상해줘", false, false, 90, 200, 60, "새벽팟 등극", "2시간 전",
                        ),
                        FeedEntity(
                            6, "", "미스터 다 빈", false, false, 90, 200, 60, "혹시 콩 좋아해?", "2시간 전",
                        ),
                        FeedEntity(
                            6, "", "이승헌금좀주세요", false, false, 90, 200, 60, "새벽 런닝좌", "2시간 전",
                        ),
                        FeedEntity(
                            6, "", "홍박사님을 아세요?", false, false, 90, 200, 60, "잠 좀 자!!", "2시간 전",
                        ),
                        FeedEntity(
                            6, "", "무소유ㄴ", false, false, 90, 200, 60, "아쿠아리움 재밌었어?", "2시간 전",
                        ),
                    ),
                )
            }

        binding.rvHome.addItemDecoration(MyPageFeedItemDecorator(requireContext()))
    }

    private fun initBottomSheet() {
        HomeBottomSheet().show(parentFragmentManager, HOME_BOTTOM_SHEET)
    }

    private fun navigateToHomeDetailFragment(feedData: Feed) {
        findNavController().navigate(
            R.id.action_home_to_home_detail,
            bundleOf(KEY_FEED_DATA to feedData),
        )
    }

    companion object {
        const val HOME_BOTTOM_SHEET = "home_bottom_sheet"
        const val KEY_FEED_DATA = "key_feed_data"
    }
}
