package com.hyf.iot.widget

import android.view.View


@Suppress("UNCHECKED_CAST")
fun <T : View> View.findViewByIdEx(id: Int): T {
    return this.findViewById(id) as T
}