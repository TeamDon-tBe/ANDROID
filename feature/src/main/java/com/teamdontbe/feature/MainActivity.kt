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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bnvMain.setupWithNavController(navController)

        removeBadge(navController)
    }

    private fun removeBadge(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragment_notification) {
                val badgeDrawable = binding.bnvMain.getBadge(R.id.fragment_notification)
                // notificationFragment 진입할때 없어짐
                badgeDrawable?.isVisible = false
                badgeDrawable?.clearNumber() // or badgeDrawable?.clearText()
            }
        }
    }

    private fun initMainBottomNaviBadge() {
        val badge = binding.bnvMain.getOrCreateBadge(R.id.fragment_notification)
        badge.isVisible = true
        // 없애면 빨강색 dot만 존재
        badge.number = 99 // or badge.text = "New"
    }
}
