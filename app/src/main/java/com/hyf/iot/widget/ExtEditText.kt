package com.hyf.iot.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import androidx.appcompat.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import com.hyf.iot.App

import com.hyf.iot.R

/**
 * 扩展的EditText组件
 * com.hyf.iot.widget.ExtEditText
 */
@Suppress("DEPRECATION")
class ExtEditText : AppCompatEditText {
    private var enableClearButton = true

    private var mContext: Context? = null

    var imgEnable= App.instance.resources.getDrawable(R.drawable.icon_delete)!!

    interface OndispatchKeyEventPreIme {
        fun dispatchKeyEventPreIme(event: KeyEvent)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
        compoundDrawablePadding = mContext!!.resources
                .getDimensionPixelSize(R.dimen.margin_5)
        addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                setDrawable(enableClearButton)
            }
        })
        setDrawable(enableClearButton)
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent): Boolean {
        if (m_diKeyEventPreIme != null) {
            m_diKeyEventPreIme!!.dispatchKeyEventPreIme(event)
        }
        return super.dispatchKeyEventPreIme(event)
    }

    fun setDispatchKeyEventPreIme(
            ondispatchKeyEventPreIme: OndispatchKeyEventPreIme?) {
        m_diKeyEventPreIme = ondispatchKeyEventPreIme
    }

    /**
     * 设置删除图片
     */
    fun setDrawable(isShow: Boolean) {
        enableClearButton = isShow
        val draw = compoundDrawables
        if (draw != null) {
            if (!enableClearButton || text.toString().isEmpty()
                    || !isFocused) {
                setCompoundDrawablesWithIntrinsicBounds(draw[0], draw[1], null,
                        draw[3])
            } else {
                setCompoundDrawablesWithIntrinsicBounds(draw[0], draw[1],
                        imgEnable, draw[3])
            }
            compoundDrawablePadding = mContext!!.resources
                    .getDimensionPixelSize(R.dimen.comm_margin)
        }
    }

    /**
     * event.getX() 获取相对应自身左上角的X坐标 event.getY() 获取相对应自身左上角的Y坐标 getWidth()
     * 获取控件的宽度 getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离 getPaddingRight()
     * 获取删除图标右边缘到控件右边缘的距离 getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (enableClearButton && event.action == MotionEvent.ACTION_UP) {
            val x = event.x.toInt()
            // 判断触摸点是否在水平范围内
            val isInnerWidth = x > width - totalPaddingRight && x < width - paddingRight
            // 获取删除图标的边界，返回一个Rect对象
            val rect = imgEnable.bounds
            // 获取删除图标的高度
            val height = rect.height()
            val y = event.y.toInt()
            // 计算图标底部到控件底部的距离
            val distance = (getHeight() - height) / 2
            // 判断触摸点是否在竖直范围内(可能会有点误差)
            // 触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
            val isInnerHeight = y > distance && y < distance + height

            if (isInnerWidth && isInnerHeight) {
                setText("")
            }

        }

        return super.onTouchEvent(event)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int,
                                previouslyFocusedRect: Rect?) {
        setDrawable(enableClearButton)
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }

    companion object {

        private var m_diKeyEventPreIme: OndispatchKeyEventPreIme? = null
    }

}
