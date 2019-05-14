package com.hyf.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import com.hyf.iot.R

class LoadingDialog : Dialog {

    constructor(context: Context,themeId: Int) : super(context,themeId)
    constructor(context: Context) : super(context)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(View.inflate(context, R.layout.dialog_loading, null))
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

}