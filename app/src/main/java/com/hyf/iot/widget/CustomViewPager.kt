package com.hyf.iot.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

//com.hyf.iot.widget.CustomViewPager
class CustomViewPager(ctx: Context, attrs: AttributeSet) : ViewPager(ctx, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val `in` = super.onInterceptTouchEvent(ev)
        if (`in`) {
            parent.requestDisallowInterceptTouchEvent(true)
            this.requestDisallowInterceptTouchEvent(true)
        }
        return false
    }
}