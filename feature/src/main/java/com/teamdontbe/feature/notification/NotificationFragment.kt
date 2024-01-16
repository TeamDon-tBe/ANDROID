package com.teamdontbe.feature.notification

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.NotiEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentNotificationBinding
import com.teamdontbe.feature.notification.adapter.NotificationAdapter
import com.teamdontbe.feature.notification.adapter.NotificationItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {
    private val notiViewModel by viewModels<NotificationViewModel>()

    override fun initView() {
        statusBarColorOf(R.color.white)
        binding.appbarNotification.tvAppbarCancel.visibility = View.INVISIBLE

        initObserve()
    }

    private fun initObserve() {
        notiViewModel.getNotiList.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> initNotificationAdapter(it.data)
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initNotificationAdapter(notiData: List<NotiEntity>) {
        if (notiData.isEmpty()) {
            binding.layoutNotificationEmpty.visibility = View.VISIBLE
        } else {
            binding.rvNotification.adapter =
                NotificationAdapter(click = { notiData, position ->
                    when (notiData.notificationTriggerType) {
                        "contentLiked" -> Unit // 바꿔야 함
                        "comment" -> Unit // 바꿔야 함
                        "commentLiked" -> Unit // 바꿔야 함
                        "actingContinue" -> findNavController().navigate(R.id.action_notification_to_posting)
                        "beGhost" -> findNavController().navigate(R.id.action_notification_to_my_page)
                        "contentGhost" -> Unit // 바꿔야 함
                        "commentGhost" -> Unit // 바꿔야 함
                        "userBan" -> Unit

                        else ->
                            Timber.tag("noti")
                                .e("등록되지 않은 노티가 감지되었습니다 : ${notiData.notificationTriggerType}")
                    }
                }).apply {
                    submitList(notiData)
                }
            binding.rvNotification.addItemDecoration(NotificationItemDecorator(requireContext()))
        }
    }
}
