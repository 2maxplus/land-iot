package com.hyf.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import com.hyf.iot.R
import com.hyf.iot.widget.CircleCountDownView

class CountDownDialog : Dialog {

    private var countDownView: CircleCountDownView? = null
    private var mListener: CountDownFinishListener? = null

    constructor(context: Context,mListener: CountDownFinishListener) : super(context){
        this.mListener = mListener
    }
    constructor(context: Context) : super(context)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View.inflate(context, R.layout.dialog_count_down, null))
        countDownView = findViewById(R.id.countDownView)
        countDownView!!.setCountDownListener(object : CircleCountDownView.CountDownListener {
            override fun onCountDownFinish() {
                dismiss()
                if(mListener != null){
                    mListener!!.onFinish()
                }
            }
            override fun restTime(restTime: Long) {
            }
        })
        countDownView!!.setAnimationInterpolator { inputFraction -> inputFraction * inputFraction }

    }

    override fun show() {
        super.show()
        countDownView!!.setStartCountValue(7)
        countDownView!!.startCountDown()
    }

    interface CountDownFinishListener {
        fun onFinish()
    }

}