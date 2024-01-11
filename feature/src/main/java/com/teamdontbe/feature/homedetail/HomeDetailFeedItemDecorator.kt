package com.teamdontbe.feature.homedetail

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.teamdontbe.core_ui.util.context.pxToDp

class HomeDetailFeedItemDecorator(val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            outRect.top = context.pxToDp(0)
            view.setPadding(
                context.pxToDp(12),
                context.pxToDp(16),
                context.pxToDp(16),
                context.pxToDp(20),
            )
        } else {
            outRect.left = context.pxToDp(16)
            outRect.right = context.pxToDp(16)
        }
        outRect.bottom = context.pxToDp(4)
    }
}
