package com.teamdontbe.feature.notification

import android.view.View
import com.teamdontbe.core_ui.base.BindingFragment
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentNotificationBinding

class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {
    override fun initView() {
        binding.appbarNotification.tvAppbarCancel.visibility = View.INVISIBLE
    }
}
