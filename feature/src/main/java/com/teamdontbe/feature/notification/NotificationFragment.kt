package com.teamdontbe.feature.notification

import android.view.View
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
//            // 새로 고침 시작 시에 RecyclerView를 약간 아래로 내리는 애니메이션
//            val translateY = 10000f // 조절 가능한 값
//            binding.rvNotification.animate().translationY(translateY).start()
//
//            notiViewModel.getNotificationList()
//
//            // 새로 고침이 완료된 후에 RecyclerView를 다시 원래 위치로 올리는 애니메이션
//            binding.rvNotification.animate().translationY(0f).withEndAction {
//                binding.swipeRefreshLayout.isRefreshing = false
//            }.start()

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

    companion object {
        const val KEY_NOTI_DATA = "key_noti_data"
    }
}
