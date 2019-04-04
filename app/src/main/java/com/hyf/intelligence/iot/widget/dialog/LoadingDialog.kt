package com.hyf.intelligence.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import com.hyf.intelligence.iot.R

class LoadingDialog(context: Context) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(View.inflate(getContext(), R.layout.dialog_loading, null))
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

}