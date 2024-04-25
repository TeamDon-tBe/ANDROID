package com.teamdontbe.feature

import android.app.Activity
import android.content.Intent
import android.os.Handler
import androidx.activity.viewModels
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.UpdateAvailability
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.core_ui.util.context.toast
import com.teamdontbe.feature.databinding.ActivitySplashBinding
import com.teamdontbe.feature.login.LoginActivity
import com.teamdontbe.feature.login.LoginViewModel
import com.teamdontbe.feature.signup.SignUpAgreeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val loginViewModel by viewModels<LoginViewModel>()
    val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)

    override fun initView() {
        initSplash()
    }

    private fun initSplash() {
        // 타이머가 끝나면 내부 실행
        Handler().postDelayed(
            Runnable {
                when {
                    loginViewModel.getNickName().isNotBlank() && loginViewModel.getAccessToken()
                        .isNotBlank() && loginViewModel.getRefreshToken()
                        .isNotBlank() && loginViewModel.checkLogin() -> {
                        navigateTo<MainActivity>()
                    }

                    loginViewModel.getNickName().isBlank() && loginViewModel.getAccessToken()
                        .isNotBlank() && loginViewModel.getRefreshToken().isNotBlank() -> {
                        navigateTo<SignUpAgreeActivity>()
                    }

                    else -> {
                        navigateTo<LoginActivity>()
                    }
                }
                finish()
            },
            800,
        ) // 3초
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != Activity.RESULT_OK) {
                toast("업데이트가 취소 되었습니다.")

                val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                    if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE) {
                        if (appUpdateInfo.isUpdateTypeAllowed(FLEXIBLE)) {
                            MaterialAlertDialogBuilder(this).setMessage(
                                "Don't be가 업데이트 되었습니다.\n" +
                                    "- 눌러서 바로 이동할 수 있는 링크를 삽입할 수 있어요.\n" +
                                    "- 내 글에 답글을 달거나 좋아요를 누른 상대의 프로필로 이동할 수 있어요.\n" +
                                    "- 그 외 자잘한 오류들을 해결했어요."
                            ).setTitle("1.1.0 업데이트 안내")
                                .setNegativeButton("지금 업데이트") { dialog, which ->
                                    requestAppUpdate()
                                }
                                .setPositiveButton("나중에") { dialog, which ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun requestAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    FLEXIBLE,
                    this,
                    REQUEST_CODE_UPDATE
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    requestAppUpdate()
                }
            }
    }

    private inline fun <reified T : Activity> navigateTo() {
        Intent(this@SplashActivity, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    companion object {
        const val REQUEST_CODE_UPDATE = 1000
    }
}
