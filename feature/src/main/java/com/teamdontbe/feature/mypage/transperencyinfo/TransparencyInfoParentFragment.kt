package com.teamdontbe.feature.mypage.transperencyinfo

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentTransperencyInfoParentBinding

class TransparencyInfoParentFragment :
    BindingDialogFragment<FragmentTransperencyInfoParentBinding>(R.layout.fragment_transperency_info_parent) {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initView() {
        /*  initContentText()
          initCancelButtonClick()*/
        initMyPageTabLayout()
    }

    private fun initContentText() {
//        binding.tvDeleteDialogContent.text = content
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 16.0f)
    }

    private fun initCancelButtonClick() {
        /* binding.btnDeleteDialogCancel.setOnClickListener {
             dismiss()
         }*/
    }

    private fun initMyPageTabLayout() = with(binding) {
        vpMyPage.adapter = TransparencyInfoVpAdapter(this@TransparencyInfoParentFragment)

        TabLayoutMediator(tabTransparencyInfoIndicator, vpMyPage) { tab, position ->
        }.attach()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}
