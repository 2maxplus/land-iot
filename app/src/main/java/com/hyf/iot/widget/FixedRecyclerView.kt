package com.hyf.iot.widget

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet

//com.hyf.iot.widget.FixedRecyclerView
class FixedRecyclerView : androidx.recyclerview.widget.RecyclerView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun canScrollVertically(direction: Int): Boolean {
        // check if scrolling up
        if (direction < 1) {
            val original = super.canScrollVertically(direction)
            return !original && getChildAt(0) != null && getChildAt(0).top < 0 || original
        }
        return super.canScrollVertically(direction)

    }
}