package com.teamdontbe.feature.home

import android.Manifest
import android.content.Intent
import android.os.Build
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.core_ui.util.AmplitudeUtil.trackEvent
import com.teamdontbe.core_ui.util.fragment.statusBarColorOf
import com.teamdontbe.core_ui.util.fragment.viewLifeCycle
import com.teamdontbe.core_ui.util.fragment.viewLifeCycleScope
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.domain.entity.FeedEntity
import com.teamdontbe.domain.entity.ProfileEditInfoEntity
import com.teamdontbe.feature.ErrorActivity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentHomeBinding
import com.teamdontbe.feature.dialog.DeleteCompleteDialogFragment
import com.teamdontbe.feature.dialog.TransparentDialogFragment
import com.teamdontbe.feature.homedetail.HomeDetailFragment.Companion.ALARM_TRIGGER_TYPE_CONTENT
import com.teamdontbe.feature.snackbar.TransparentIsGhostSnackBar
import com.teamdontbe.feature.util.AmplitudeTag.CLICK_POST_LIKE
import com.teamdontbe.feature.util.AmplitudeTag.CLICK_POST_VIEW
import com.teamdontbe.feature.util.EventObserver
import com.teamdontbe.feature.util.FeedItemDecorator
import com.teamdontbe.feature.util.KeyStorage
import com.teamdontbe.feature.util.KeyStorage.DELETE_POSTING
import com.teamdontbe.feature.util.PagingLoadingAdapter
import com.teamdontbe.feature.util.pagingSubmitData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel by activityViewModels<HomeViewModel>()

    private lateinit var homeFeedAdapter: HomePagingFeedAdapter

    override fun initView() {
        statusBarColorOf(R.color.gray_1)
        initPushAlarmPermissionAlert()
        observePatchUserPushAlarmInfo()
        initHomeFeedAdapter()
        observeOpenHomeDetail()
        observePostTransparentStatus()
        observeDeleteFeedStatus()
        initSwipeRefreshData()
        scrollRecyclerViewToTop()
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> handlePushAlarmPermissionGranted()
            false -> handlePushAlarmPermissionDenied()
        }
    }

    private fun initPushAlarmPermissionAlert() {
        if (homeViewModel.getIsPushAlarmAllowed() == null && homeViewModel.checkLogin()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissionList = Manifest.permission.POST_NOTIFICATIONS
                requestPermission.launch(permissionList)
            } else {
                handlePushAlarmPermissionGranted()
            }
        }
    }

    private fun handlePushAlarmPermissionGranted() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    homeViewModel.patchUserProfileUri(
                        ProfileEditInfoEntity(
                            isPushAlarmAllowed = true,
                            fcmToken = task.result
                        )
                    )
                    Timber.tag("fcm").d("fcm token: $task.result")
                } else {
                    Timber.d(task.exception)
                    return@OnCompleteListener
                }
            }
        )
    }

    private fun handlePushAlarmPermissionDenied() {
        homeViewModel.patchUserProfileUri(ProfileEditInfoEntity(isPushAlarmAllowed = false))
    }

    private fun observePatchUserPushAlarmInfo() {
        homeViewModel.pushAlarmAllowedStatus.flowWithLifecycle(viewLifeCycle).onEach {
            homeViewModel.saveIsPushAlarmAllowed(it)
        }.launchIn(viewLifeCycleScope)
    }

    private fun initHomeFeedAdapter() {
        homeFeedAdapter = HomePagingFeedAdapter(
            context = requireContext(),
            onClickKebabBtn = ::onKebabBtnClick,
            onClickLikedBtn = ::onLikedBtnClick,
            onClickTransparentBtn = ::onTransparentBtnClick,
            onClickUserProfileBtn = ::navigateToMyPageFragment,
            onClickToNavigateToHomeDetail = { feedData ->
                trackEvent(CLICK_POST_VIEW)
                homeViewModel.openHomeDetail(feedData)
            },
            userId = homeViewModel.getMemberId(),
            onClickFeedImage = { navigateToImageDetailFragment(it) },
        )
        homeFeedAdapter.apply {
            pagingSubmitData(
                viewLifecycleOwner,
                homeViewModel.getFeedList(),
                homeFeedAdapter
            )
        }
        setRecyclerViewItemDecoration()
        binding.rvHome.adapter = homeFeedAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoadingAdapter(),
            footer = PagingLoadingAdapter()
        )
    }

    private fun onKebabBtnClick(
        feedData: FeedEntity,
        position: Int,
    ) {
        feedData.contentId?.let {
            initBottomSheet(
                feedData.memberId == homeViewModel.getMemberId(),
                it,
                feedData.memberNickname,
                feedData.contentText
            )
        }
    }

    private fun initBottomSheet(
        isMember: Boolean,
        contentId: Int,
        reportTargetNickname: String,
        relateText: String,
    ) {
        HomeBottomSheet(isMember, contentId, false, -1, reportTargetNickname, relateText).show(
            parentFragmentManager,
            HOME_BOTTOM_SHEET,
        )
    }

    private fun onLikedBtnClick(
        contentId: Int,
        status: Boolean,
    ) {
        if (status) {
            homeViewModel.deleteFeedLiked(contentId)
        } else {
            trackEvent(CLICK_POST_LIKE)
            homeViewModel.postFeedLiked(contentId)
        }
    }

    private fun onTransparentBtnClick(data: FeedEntity) {
        if (data.isGhost) {
            TransparentIsGhostSnackBar.make(binding.root).show()
        } else {
            initTransparentDialog(data.memberId, data.contentId ?: -1)
        }
    }

    private fun initTransparentDialog(
        targetMemberId: Int,
        alarmTriggerId: Int,
    ) {
        val dialog = TransparentDialogFragment(
            ALARM_TRIGGER_TYPE_CONTENT,
            targetMemberId,
            alarmTriggerId
        )
        dialog.show(childFragmentManager, HOME_TRANSPARENT_DIALOG)
    }

    private fun navigateToMyPageFragment(memberId: Int) {
        findNavController().navigate(
            R.id.action_fragment_home_to_fragment_my_page,
            bundleOf(KEY_FEED_DATA to memberId),
        )
    }

    private fun observeOpenHomeDetail() {
        homeViewModel.openHomeDetail.observe(
            viewLifecycleOwner,
            EventObserver {
                navigateToHomeDetailFragment(it)
            },
        )
    }

    private fun navigateToHomeDetailFragment(feedData: FeedEntity) {
        findNavController().navigate(
            R.id.action_home_to_home_detail,
            bundleOf(KEY_HOME_DETAIL_FEED to Feed(feedData)),
        )
    }

    private fun setRecyclerViewItemDecoration() {
        if (binding.rvHome.itemDecorationCount == 0) {
            binding.rvHome.addItemDecoration(
                FeedItemDecorator(requireContext()),
            )
        }
    }

    private fun navigateToErrorPage() {
        startActivity(Intent(requireActivity(), ErrorActivity::class.java))
    }

    private fun observePostTransparentStatus() {
        homeViewModel.postTransparent.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> homeFeedAdapter.refresh()
                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun observeDeleteFeedStatus() {
        homeViewModel.deleteFeed.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is UiState.Success -> handleDeleteFeedSuccess()
                is UiState.Failure -> navigateToErrorPage()
                else -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun handleDeleteFeedSuccess() {
        homeFeedAdapter.refresh()
        val dialog = DeleteCompleteDialogFragment()
        dialog.show(childFragmentManager, DELETE_POSTING)
    }

    private fun initSwipeRefreshData() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            val slideDown =
                AnimationUtils.loadAnimation(context, R.anim.anim_swipe_refresh_slide_down)
            val slideUp =
                AnimationUtils.loadAnimation(context, R.anim.anim_swipe_refresh_slide_up)

            slideDown.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {
                        // slideDown 애니메이션이 끝나면 slideUp 애니메이션 실행
                        binding.rvHome.startAnimation(slideUp)
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                },
            )
            binding.rvHome.startAnimation(slideDown)

            homeFeedAdapter.refresh()
            scrollRecyclerViewToTopWhenObserveDataRefresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun scrollRecyclerViewToTopWhenObserveDataRefresh() {
        homeFeedAdapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    if (positionStart == 0) scrollRecyclerViewToTop()
                    homeFeedAdapter.unregisterAdapterDataObserver(this)
                }
            })
    }

    fun scrollRecyclerViewToTop() {
        binding.rvHome.post {
            binding.rvHome.smoothScrollToPosition(0)
        }
    }

    private fun navigateToImageDetailFragment(it: String) {
        findNavController().navigate(
            R.id.fragment_image_detail,
            bundleOf(KeyStorage.KEY_NOTI_DATA to it),
        )
    }

    companion object {
        const val HOME_BOTTOM_SHEET = "home_bottom_sheet"
        const val HOME_TRANSPARENT_DIALOG = "home_transparent_dialog"
        const val KEY_FEED_DATA = "key_feed_data"
        const val KEY_HOME_DETAIL_FEED = "key_home_detail_feed"
    }
}
