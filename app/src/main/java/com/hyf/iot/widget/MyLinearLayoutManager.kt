package com.hyf.iot.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet

class MyLinearLayoutManager : LinearLayoutManager {
    private var isScrollEnabled = true

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context,orientation,reverseLayout)

    fun setScrollEnabled(flag: Boolean) {
        this.isScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled && super.canScrollVertically()
    }
}