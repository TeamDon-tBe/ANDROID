package com.teamdontbe.feature.snackbar

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.teamdontbe.feature.R
import com.teamdontbe.feature.databinding.SnackbarLinkCountErrorBinding

class LinkCountErrorSnackBar(view: View) {
    companion object {
        fun make(view: View) = LinkCountErrorSnackBar(view)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 1200)
    private val snackbarLayout = snackbar.view as? Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val binding: SnackbarLinkCountErrorBinding =
        DataBindingUtil.inflate(inflater, R.layout.snackbar_link_count_error, null, false)

    init {
        initView()
    }

    private fun initView() {
        snackbarLayout?.run {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
        }
    }

    fun setText(text: String) {
        binding.tvCommentLinkCountError.text = text
    }

    fun show() {
        snackbar.show()
    }
}
