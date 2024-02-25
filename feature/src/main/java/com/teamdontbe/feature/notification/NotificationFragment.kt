package com.teamdontbe.feature.notification

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
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
import com.teamdontbe.feature.util.KeyStorage.KEY_NOTI_DATA
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

        notiViewModel.getNotificationList()
        initObserveList()
        initObserveCheck()

        initSwipeRefreshData()
    }

    private fun initSwipeRefreshData() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            val slideDown =
                AnimationUtils.loadAnimation(context, R.anim.anim_swipe_refresh_slide_down)
            val slideUp = AnimationUtils.loadAnimation(context, R.anim.anim_swipe_refresh_slide_up)

            slideDown.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {
                        // slideDown 애니메이션이 끝나면 slideUp 애니메이션 실행
                        binding.rvNotification.startAnimation(slideUp)
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                },
            )
            binding.rvNotification.startAnimation(slideDown)

            notiViewModel.getNotificationList()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initObserveCheck() {
        notiViewModel.patchNotiCheck.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> Timber.tag("noti").i("patch 성공 : ${it.data}")
                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initObserveList() {
        notiViewModel.getNotiList.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    initNotificationAdapter(it.data)
                    notiViewModel.patchNotificationCheck()
                }

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
                        "contentLiked" -> navigateToHomeDetailFragment(notiData)
                        "comment" -> navigateToHomeDetailFragment(notiData)
                        "commentLiked" -> navigateToHomeDetailFragment(notiData)
                        "actingContinue" -> findNavController().navigate(R.id.action_notification_to_posting)
                        "beGhost" -> findNavController().navigate(R.id.action_notification_to_my_page)
                        "contentGhost" -> navigateToHomeDetailFragment(notiData)
                        "commentGhost" -> navigateToHomeDetailFragment(notiData)
                        "userBan" -> Unit

                        else ->
                            Timber.tag("noti")
                                .e("등록되지 않은 노티가 감지되었습니다 : ${notiData.notificationTriggerType}")
                    }
                }).apply {
                    submitList(notiData)
                }
            if (binding.rvNotification.itemDecorationCount == 0) {
                binding.rvNotification.addItemDecoration(
                    NotificationItemDecorator(requireContext()),
                )
            }
        }
    }

    private fun navigateToHomeDetailFragment(notiData: NotiEntity) {
        findNavController().navigate(
            R.id.action_notification_to_home_detail,
            bundleOf(KEY_NOTI_DATA to notiData.notificationTriggerId),
        )
    }
}
