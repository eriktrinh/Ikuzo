package com.eriktrinh.ikuzo.ui

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacingItemDecoration(val dividerSpace: Int, val endingSpace: Int? = null, val horizontal: Boolean = false) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemCount = parent.adapter.itemCount
        val position = parent.getChildAdapterPosition(view)
        if (position == 0 && endingSpace != null) {
            if (horizontal) {
                outRect.left = endingSpace
            } else {
                outRect.top = endingSpace
            }
        }
        if (position != itemCount - 1) {
            if (horizontal) {
                outRect.right = dividerSpace
            } else {
                outRect.bottom = dividerSpace
            }
        } else if (endingSpace != null) {
            if (horizontal) {
                outRect.right = endingSpace
            } else {
                outRect.bottom = endingSpace
            }

        }
    }
}