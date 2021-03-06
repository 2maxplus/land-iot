package com.hyf.iot.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.AbsListView
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


/***
 * com.hyf.intelligence.kotlin.widget.VpSwipeRefreshLayout
 * 解决SwipeRefreshLayout和ViewPager滑动冲突
 */
@Suppress("DEPRECATION")
class VpSwipeRefreshLayout(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    private var startY: Float = 0.toFloat()
    private var startX: Float = 0.toFloat()
    // 记录viewPager是否拖拽的标记
    private var mIsVpDragger: Boolean = false
    private val mTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录手指按下的位置
                startY = ev.y
                startX = ev.x
                // 初始化标记
                mIsVpDragger = false
            }
            MotionEvent.ACTION_MOVE -> {
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDragger) {
                    return false
                }
                // 获取当前手指位置
                val endY = ev.y
                val endX = ev.x
                val distanceX = Math.abs(endX - startX)
                val distanceY = Math.abs(endY - startY)
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDragger = true
                    return false
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                // 初始化标记
                mIsVpDragger = false
        }
        // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev)
    }

    override fun canChildScrollUp(): Boolean {
        val target = getChildAt(0)
        return if (target is AbsListView) {
            target.childCount > 0 && (target.firstVisiblePosition > 0 || target.getChildAt(0)
                    .top < target.paddingTop)
        } else
            ViewCompat.canScrollVertically(target, -1)
    }

    override fun requestDisallowInterceptTouchEvent(b: Boolean) {
        super.requestDisallowInterceptTouchEvent(b)
    }
}