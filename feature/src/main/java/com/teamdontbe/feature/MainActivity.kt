package com.teamdontbe.feature

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
    }

    private fun removeBadgeOnNotification(navController: NavController) {
        val badgeDrawable = binding.bnvMain.getBadge(R.id.fragment_notification)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragment_notification) {
                badgeDrawable?.apply {
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
}
