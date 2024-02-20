package com.finance.movieslisttmdb.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomGridLayoutManager(context: Context, spanCount: Int) :
    GridLayoutManager(context, spanCount) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        if (state?.itemCount == 0 || state?.isPreLayout == true) {
            return
        }

        val childWidth = (width - paddingLeft - paddingRight) / spanCount
        var xOffset = paddingLeft
        var yOffset = paddingTop

        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            val lp = child.layoutParams as RecyclerView.LayoutParams

            if (xOffset + childWidth > width - paddingRight) {
                xOffset = paddingLeft
                yOffset += child.measuredHeight + lp.topMargin + lp.bottomMargin
            }

            layoutDecorated(
                child,
                xOffset,
                yOffset,
                xOffset + childWidth,
                yOffset + child.measuredHeight
            )
            xOffset += childWidth + lp.leftMargin + lp.rightMargin
        }
    }
}
