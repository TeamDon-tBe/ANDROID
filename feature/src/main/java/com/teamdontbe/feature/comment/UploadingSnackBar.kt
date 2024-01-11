package com.teamdontbe.feature.comment

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.SnackbarPostingLoadingBinding

class UploadingSnackBar(view: View) {
    companion object {
        fun make(view: View) = UploadingSnackBar(view)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 1000)
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
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
        }
    }

    fun show() {
        snackbar.show()
    }
}
