package com.teamdontbe.feature.dialog

import android.os.Handler
import android.os.Looper
import com.teamdontbe.core_ui.base.BindingDialogFragment
import com.teamdontbe.core_ui.util.context.dialogFragmentResize
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.FragmentDeleteCompleteDialogBinding

class DeleteCompleteDialogFragment() :
    BindingDialogFragment<FragmentDeleteCompleteDialogBinding>(R.layout.fragment_delete_complete_dialog) {
    override fun initView() {
        setDismiss()
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 25.0f)
    }

    private fun setDismiss() {
        Handler(Looper.getMainLooper()).postDelayed({
            dismiss()
        }, 2000) // 1초 대기
    }
}
