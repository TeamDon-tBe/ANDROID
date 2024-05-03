package com.teamdontbe.feature.snackbar

import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.SnackbarPostingLoadingBinding

class UploadingSnackBar(view: View) {
    companion object {
        fun make(view: View) = UploadingSnackBar(view)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 1600)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val binding: SnackbarPostingLoadingBinding =
        DataBindingUtil.inflate(inflater, R.layout.snackbar_posting_loading, null, false)

    init {
        initView()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(16, 0, 16, 4)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
        }
    }

    fun setSnackbarPadding(left: Int, top: Int, right: Int, bottom: Int) {
        snackbarLayout.setPadding(left, top, right, bottom)
    }

    private fun setSnackbarToComplete() {
        with(binding) {
            ivSnackbarPostingLoadingCheck.isVisible = true
            progressPostingLoading.isVisible = false
            tvPostingLoading.text = "게시 완료!"
            clSnackbarPostingLoading.backgroundTintList =
                ColorStateList.valueOf(context.getColor(R.color.primary))
        }
    }

    fun show(paddingLeft: Int = 0, paddingTop: Int = 0, paddingRight: Int = 0, paddingBottom: Int = 0) {
        setSnackbarPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        snackbar.show()
        Handler(Looper.getMainLooper()).postDelayed({
            setSnackbarToComplete()
        }, 1000) // 1초 대기
    }
}
