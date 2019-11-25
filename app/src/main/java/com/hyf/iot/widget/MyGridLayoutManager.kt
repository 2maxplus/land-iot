package com.hyf.iot.widget

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import android.util.AttributeSet

class MyGridLayoutManager : androidx.recyclerview.widget.GridLayoutManager {
    private var isScrollEnabled = true

    constructor(context: Context, spanCount: Int) : super(context, spanCount) {}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(context, spanCount, orientation, reverseLayout) {}

    fun setScrollEnabled(flag: Boolean) {
        this.isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}