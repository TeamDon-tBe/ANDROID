package com.teamdontbe.feature.notification

import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentNotificationBinding
import com.teamdontbe.feature.notification.adapter.NotificationAdapter
import com.teamdontbe.feature.notification.adapter.NotificationItemDecorator

class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {
    override fun initView() {
        initNotificationAdapter()
    }

    private fun initNotificationAdapter() {
        binding.rvNotification.adapter =
            NotificationAdapter(click = { notiData, position ->
            }).apply {
                submitList(
                    listOf(
                        NotiEntity(
                            // 게시물 좋아요에 대한 노티
                            1,
                            "돈비짱짱맨",
                            "돈비최고",
                            "example",
                            "contentLiked",
                            "2024-01-11 02:44:07",
                            3,
                            "",
                            false,
                        ),
                        NotiEntity(
                            // 답글 작성에 대한 노티
                            1,
                            "",
                            "돈비최고",
                            "example",
                            "comment",
                            "2024-01-11 02:44:07",
                            3,
                            "상대방이 작성한 답글 내용",
                            false,
                        ),
                        NotiEntity(
                            // 답글 좋아요에 대한 노티
                            1,
                            "돈비짱짱맨",
                            "돈비최고",
                            "example",
                            "commentLiked",
                            "2024-01-11 02:44:07",
                            3,
                            "",
                            false,
                        ),
                        NotiEntity(
                            // 활동 재개 안내에 대한 노티
                            1,
                            "돈비짱짱맨",
                            "",
                            "ghost image url",
                            "actingContinue",
                            "2024-01-11 02:44:07",
                            0,
                            "",
                            false,
                        ),
                        NotiEntity(
                            // 활동 제한 안내에 대한 노티
                            1,
                            "돈비짱짱맨",
                            "",
                            "ghost image url",
                            "beGhost",
                            "2024-01-11 02:44:07",
                            0,
                            "",
                            false,
                        ),
                        NotiEntity(
                            // 게시글 투명도에 대한 노티
                            1,
                            "돈비짱짱맨",
                            "",
                            "ghost image url",
                            "contentGhost",
                            "2024-01-11 02:44:07",
                            0,
                            "",
                            false,
                        ),
                        NotiEntity(
                            // 답글 투명도에 대한 노티
                            1,
                            "돈비짱짱맨",
                            "",
                            "ghost image url",
                            "commentGhost",
                            "2024-01-11 02:44:07",
                            0,
                            "",
                            false,
                        ),
                        NotiEntity(
                            // 유저 벤에 대한 노티
                            1,
                            "돈비짱짱맨",
                            "",
                            "ghost image url",
                            "userBan",
                            "2024-01-11 02:44:07",
                            0,
                            "",
                            false,
                        ),
                    ),
                )
            }
        binding.rvNotification.addItemDecoration(NotificationItemDecorator(requireContext()))
    }
}
