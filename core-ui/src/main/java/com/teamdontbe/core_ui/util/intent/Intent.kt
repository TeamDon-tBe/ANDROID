package com.teamdontbe.core_ui.util.intent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build

fun <T> Intent.getParcelable(name: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, clazz)
    } else {
        getParcelableExtra(name)
    }
}

inline fun <reified T : Activity> navigateTo(context: Context) {
    Intent(context, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(this)
    }
}

