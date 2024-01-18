package com.teamdontbe.feature

import android.content.ContentValues
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.kakao.sdk.user.UserApiClient
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.intent.getParcelable
import com.teamdontbe.core_ui.view.UiState
import com.teamdontbe.feature.databinding.ActivityMainBinding
import com.teamdontbe.feature.dialog.HomePostingRestrictionDialogFragment
import com.teamdontbe.feature.notification.NotificationViewModel
import com.teamdontbe.feature.signup.SignUpAgreeActivity.Companion.SIGN_UP_AGREE
import com.teamdontbe.feature.signup.UserProfileModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val notiViewModel by viewModels<NotificationViewModel>()

    override fun initView() {
        initKakaoUser()
        initMainBottomNavigation()
        notiViewModel.getNotificationCount()
        initObserve()
        initGetUserProfile()
    }

    private fun initGetUserProfile() {
        val userProfileInfo =
            intent.getParcelable(SIGN_UP_AGREE, UserProfileModel::class.java)

        if (userProfileInfo != null) {
        }
    }

    private fun initObserve() {
        notiViewModel.getNotiCount.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is UiState.Loading -> Unit
                is UiState.Success -> {
                    if (it.data > 0) {
                        initMainBottomNaviBadge()
                    } else if (it.data == -1) {
                        Timber.tag("noti").e("알맞지 않은 noti count get : ${it.data}")
                    }
                }

                is UiState.Empty -> Unit
                is UiState.Failure -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun initBottomNavPostingClickListener(navController: NavController) {
        if (false) {
            binding.bnvMain.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.fragment_posting -> {
                        initCheckBtnClickListener()
                        false
                    }

                    else -> it.onNavDestinationSelected(navController)
                }
            }
        }
    }

    private fun initCheckBtnClickListener() {
        val dialog = HomePostingRestrictionDialogFragment()
        dialog.show(supportFragmentManager, RESTRICTION_POSTING)
    }

    private fun initKakaoUser() {
        // 카카오 유저 정보 통신 확인
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Timber.tag(ContentValues.TAG).e(error, "사용자 정보 요청 실패")
            } else if (user != null) {
                Timber.tag(ContentValues.TAG).i("사용자 정보 요청 성공")
                Timber.tag("kakao").i("kakao user id : %s", user.id)
                Timber.tag("kakao")
                    .i("kakao user nickname : %s", user.kakaoAccount?.profile?.nickname)
            }
        }
    }

    private fun initMainBottomNavigation() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment).findNavController()
        binding.bnvMain.apply {
            setupWithNavController(navController)
            itemIconTintList = null
        }

        // NotificationFragment 진입할 때 badge를 없애는 함수
        removeBadgeOnNotification(navController)
        setBottomNaviVisible(navController)

        initBottomNavPostingClickListener(navController)
    }

    private fun removeBadgeOnNotification(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragment_notification) {
                binding.bnvMain.getBadge(R.id.fragment_notification)?.apply {
                    isVisible = false
                    clearNumber() // or badgeDrawable?.clearText()
                }
            }
        }
    }

    private fun initMainBottomNaviBadge() {
        binding.bnvMain.getOrCreateBadge(R.id.fragment_notification).apply {
            isVisible = true
            backgroundColor = resources.getColor(R.color.error)
        }
    }

    private fun setBottomNaviVisible(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bnvMain.visibility =
                if (destination.id in
                    listOf(
                        R.id.fragment_home,
                        R.id.fragment_notification,
                        R.id.fragment_my_page,
                        R.id.fragment_home_detail,
                    )
                ) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    companion object {
        const val RESTRICTION_POSTING = "restriction_posting"
    }
}
