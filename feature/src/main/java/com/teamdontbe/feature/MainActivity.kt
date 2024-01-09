package com.teamdontbe.feature

import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.teamdontbe.core_ui.base.BindingActivity
import com.teamdontbe.feature.databinding.ActivityMainBinding

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
        initMainBottomNavigation()
        initMainBottomNaviBadge()
    }

    private fun initMainBottomNavigation() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment).findNavController()
        binding.bnvMain.setupWithNavController(navController)
        // NotificationFragment 진입할 때 badge를 없애는 함수
        removeBadgeOnNotification(navController)
        setBottomNaviVisible(navController)
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
            number = 99 // or badge.text = "New"
        }
    }

    private fun setBottomNaviVisible(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bnvMain.visibility =
                if (destination.id in
                    listOf(
                        R.id.fragment_home,
                        R.id.fragment_posting,
                        R.id.fragment_notification,
                        R.id.fragment_my_page,
                    )
                ) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }
}
