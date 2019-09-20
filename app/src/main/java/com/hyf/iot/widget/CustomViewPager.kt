package com.hyf.iot.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

//com.hyf.iot.widget.CustomViewPager
class CustomViewPager(ctx: Context, attrs: AttributeSet) : ViewPager(ctx, attrs) {

    //    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
////        val `in` = super.onInterceptTouchEvent(ev)
////        if (`in`) {
////            parent.requestDisallowInterceptTouchEvent(true)
////            this.requestDisallowInterceptTouchEvent(true)
////        }
//        return true
//    }
    override fun onInterceptHoverEvent(event: MotionEvent): Boolean {
        val interceptTouchEvent = super.onInterceptTouchEvent(event)
        var preX = 0f
        if (event.action == MotionEvent.ACTION_DOWN);
        run {
            preX = event.x
        }
        run {
            if (abs(event.x - preX) > 4) {
                return true
            } else {
                preX = event.x
            }
        }
        return interceptTouchEvent
    }


    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return if (v !== this && v is ViewPager) {
            true
        } else super.canScroll(v, checkV, dx, x, y)
    }
}