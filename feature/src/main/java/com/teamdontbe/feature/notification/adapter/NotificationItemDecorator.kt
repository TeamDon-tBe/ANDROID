package com.teamdontbe.feature.notification.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.core_ui.util.context.pxToDp

class NotificationItemDecorator(val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            outRect.top = context.pxToDp(6)
        } else {
            outRect.top = 0
        }
        outRect.bottom = context.pxToDp(4)
    }
}
