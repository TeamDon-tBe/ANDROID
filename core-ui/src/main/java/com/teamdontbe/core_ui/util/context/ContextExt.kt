package com.teamdontbe.core_ui.util.context

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.snackBar(
    anchorView: View,
    message: () -> String,
) {
    Snackbar.make(anchorView, message(), Snackbar.LENGTH_SHORT).show()
}

fun Context.stringOf(
    @StringRes resId: Int,
) = getString(resId)

fun Context.colorOf(
    @ColorRes resId: Int,
) = ContextCompat.getColor(this, resId)

fun Context.drawableOf(
    @DrawableRes resId: Int,
) = ContextCompat.getDrawable(this, resId)

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.openKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}

fun Context.dialogFragmentResize(
    dialogFragment: DialogFragment,
    horizontalMargin: Float,
) {
    val dpToPixel = Resources.getSystem().displayMetrics.density
    val dialogHorizontalMarginInPixels = (dpToPixel * horizontalMargin + 0.5f).toInt() // 반올림 처리
    val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
    dialogFragment.dialog?.window?.setLayout(
        deviceWidth - 2 * dialogHorizontalMarginInPixels,
        WindowManager.LayoutParams.WRAP_CONTENT,
    )
    dialogFragment.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}

fun Context.pxToDp(px: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        px.toFloat(),
        resources.displayMetrics,
    ).toInt()
}

fun Context.statusBarColorOf(
    @ColorRes resId: Int,
) {
    if (this is Activity) {
        window?.statusBarColor = colorOf(resId)
    }
}

fun Context.uriToTempFile(uri: Uri?): File? {
    if (uri == null) return null

    // 파일 스트림으로 uri로 접근해 비트맵을 디코딩
    val bitmap = contentResolver.openInputStream(uri).use {
        BitmapFactory.decodeStream(it)
    } ?: return null

    // 캐시 파일 생성
    val tempFile = File.createTempFile("file", ".jpg", cacheDir)

    // 파일 스트림을 통해 파일에 비트맵 저장
    FileOutputStream(tempFile).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, it)
    }

    return tempFile
}
