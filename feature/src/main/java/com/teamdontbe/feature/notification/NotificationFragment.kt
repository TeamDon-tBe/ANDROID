package com.teamdontbe.feature.notification

import android.view.View
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentNotificationBinding
import com.teamdontbe.feature.notification.adapter.NotificationAdapter
import com.teamdontbe.feature.notification.adapter.NotificationItemDecorator

class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {
    override fun initView() {
        statusBarColorOf(R.color.white)
        binding.appbarNotification.tvAppbarCancel.visibility = View.INVISIBLE
        initNotificationAdapter()
    }

    private fun initNotificationAdapter() {
        if (notificationFeedList.isEmpty()) {
            binding.layoutNotificationEmpty.visibility = View.VISIBLE
        } else {
            binding.rvNotification.adapter =
                NotificationAdapter(click = { notiData, position ->
                }).apply {
                    submitList(notificationFeedList)
                }
            binding.rvNotification.addItemDecoration(NotificationItemDecorator(requireContext()))
        }
    }

    private val notificationFeedList: List<NotiEntity> =
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
                "2023-01-11 02:44:07",
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
                "2023-01-12 20:44:07",
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
                "2000-01-11 02:44:07",
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
                "2024-01-01 02:44:07",
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
                "2023-12-11 02:44:07",
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
                "2023-06-11 02:44:07",
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
                "2020-06-11 02:44:07",
                0,
                "",
                false,
            ),
            // 한 번 더!
            NotiEntity(
                // 게시물 좋아요에 대한 노티
                1,
                "돈비짱짱맨",
                "돈비최고",
                "example",
                "contentLiked",
                "2024-01-12 02:44:07",
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
                "2024-01-12 17:24:07",
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
                "2024-01-12 17:23:07",
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
                "2024-01-11 17:44:07",
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
                "2024-01-11 17:27:07",
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
                "2024-01-11 17:26:07",
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
                "2024-01-11 17:28:07",
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
                "2024-01-12 17:33:07",
                0,
                "",
                false,
            ),
        )
}
