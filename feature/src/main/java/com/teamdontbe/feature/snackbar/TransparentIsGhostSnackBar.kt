package com.teamdontbe.feature.snackbar

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.SnackbarTransparentIsghostBinding

class TransparentIsGhostSnackBar(view: View) {
    companion object {
        fun make(view: View) = TransparentIsGhostSnackBar(view)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 2000)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val binding: SnackbarTransparentIsghostBinding =
        DataBindingUtil.inflate(inflater, R.layout.snackbar_transparent_isghost, null, false)

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
