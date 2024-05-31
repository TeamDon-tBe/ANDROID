package com.teamdontbe.feature.mypage.authinfo

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.viewModels
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.navigateToAppSettings
import com.teamdontbe.domain.entity.ProfileEditInfoEntity
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.ActivityPushAlarmBinding
import com.teamdontbe.feature.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MyPagePushAlarmActivity :
    BindingActivity<ActivityPushAlarmBinding>(R.layout.activity_push_alarm) {
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun initView() {
        binding.layoutPushAlarmAppbar.tvAppbarTitle.text =
            getString(R.string.tv_push_alarm_appbar_title)
        setPushAlarmSettingText()
        initPushAlarmSettingClickListener()
        initBackBtnClickListener()
        observePatchUserPushAlarmInfo()
    }

    override fun onRestart() {
        super.onRestart()
        refreshPushAlarmPermission()
    }

    private fun setPushAlarmSettingText() {
        binding.tvPushAlarmSetting.text =
            if (checkIsPushAlarmAllowed()) getString(R.string.tv_push_alarm_setting_on) else getString(
                R.string.tv_push_alarm_setting_off
            )
    }

    private fun checkIsPushAlarmAllowed(): Boolean {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(this).areNotificationsEnabled()
        }
    }

    private fun refreshPushAlarmPermission() {
        setPushAlarmSettingText()
        when (checkIsPushAlarmAllowed()) {
            true -> handlePushAlarmPermissionGranted()
            false -> handlePushAlarmPermissionDenied()
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
        homeViewModel.pushAlarmAllowedStatus.flowWithLifecycle(lifecycle).onEach {
            homeViewModel.saveIsPushAlarmAllowed(it)
        }.launchIn(lifecycleScope)
    }

    private fun initPushAlarmSettingClickListener() {
        binding.tvPushAlarmSetting.setOnClickListener {
            navigateToAppSettings()
        }
        binding.ivPushAlarmSetting.setOnClickListener {
            navigateToAppSettings()
        }
    }

    private fun initBackBtnClickListener() {
        binding.layoutPushAlarmAppbar.btnAppbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
