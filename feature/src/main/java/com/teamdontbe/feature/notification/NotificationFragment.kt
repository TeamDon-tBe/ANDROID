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
import com.teamdontbe.feature.notification.adapter.NotificationItemDecorator
import com.teamdontbe.feature.notification.adapter.NotificationPagingAdapter
import com.teamdontbe.feature.util.KeyStorage.KEY_FEED_DATA
import com.teamdontbe.feature.util.KeyStorage.KEY_NOTI_DATA
import com.teamdontbe.feature.util.PagingLoadingAdapter
import com.teamdontbe.feature.util.pagingSubmitData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {
    private val notiViewModel by viewModels<NotificationViewModel>()
    private var notiAdapter =
        NotificationPagingAdapter(click = { notiData, position -> }, onClickUserProfileBtn = {})

    override fun initView() {
        statusBarColorOf(R.color.white)
        binding.appbarNotification.tvAppbarCancel.visibility = View.INVISIBLE

        initNotificationAdapter()
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

            // 수정 해야 함
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

    private fun initNotificationAdapter() {
        notiAdapter =
            NotificationPagingAdapter(click = { notiData, position ->
                when (notiData.notificationTriggerType) {
                    "contentLiked" -> navigateToHomeDetailFragment(notiData)
                    "comment" -> navigateToHomeDetailFragment(notiData)
                    "commentLiked" -> navigateToHomeDetailFragment(notiData)
                    "actingContinue" -> findNavController().navigate(R.id.action_notification_to_posting)
                    "beGhost" -> findNavController().navigate(R.id.action_notification_to_my_page)
                    "contentGhost" -> navigateToHomeDetailFragment(notiData)
                    "commentGhost" -> navigateToHomeDetailFragment(notiData)
                    "userBan" -> Unit
                    "popularWriter" -> navigateToHomeDetailFragment(notiData)
                    "popularContent" -> navigateToHomeDetailFragment(notiData)

                    else ->
                        Timber.tag("noti")
                            .e("등록되지 않은 노티가 감지되었습니다 : ${notiData.notificationTriggerType}")
                }
            }, onClickUserProfileBtn = ::navigateToMyPageFragment)
        notiAdapter.apply {
            pagingSubmitData(
                viewLifecycleOwner,
                notiViewModel.getNotificationList(),
                notiAdapter,
            )
        }

        if (binding.rvNotification.itemDecorationCount == 0) {
            binding.rvNotification.addItemDecoration(
                NotificationItemDecorator(requireContext()),
            )
        }

        binding.rvNotification.adapter =
            notiAdapter.withLoadStateFooter(footer = PagingLoadingAdapter())

        notiAdapter.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.append.endOfPaginationReached) {
                // 아이템 수가 1보다 작으면 Empty View 보이도록 처리
                if (notiAdapter.itemCount < 1) {
                    binding.layoutNotificationEmpty.visibility = View.VISIBLE
                } else {
                    // 아이템 수가 1보다 크면 Empty View 안보이도록 처리
                    binding.layoutNotificationEmpty.visibility = View.INVISIBLE
                }
            }
        }

        notiViewModel.patchNotificationCheck()
    }

    private fun navigateToHomeDetailFragment(notiData: NotiEntity) {
        findNavController().navigate(
            R.id.action_notification_to_home_detail,
            bundleOf(KEY_NOTI_DATA to notiData.notificationTriggerId),
        )
    }

    private fun navigateToMyPageFragment(memberId: Int) {
        findNavController().navigate(
            R.id.action_notification_to_my_page,
            bundleOf(KEY_FEED_DATA to memberId),
        )
    }
}
