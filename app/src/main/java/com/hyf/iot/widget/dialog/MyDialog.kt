package com.hyf.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.hyf.iot.R


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyDialog : Dialog {

    private var title: String? = null
    private var content: String? = null
    private var leftStr: String? = null
    private var rightStr: String? = null

    private var listener: View.OnClickListener? = null


    constructor(context: Context?, content: String, listener: View.OnClickListener) : super(context, R.style.MyDialog) {
        this.content = content
        this.listener = listener
    }

    constructor(context: Context, title: String, content: String, listener: View.OnClickListener) : super(context, R.style.MyDialog) {
        this.title = title
        this.content = content
        this.listener = listener
    }

    constructor(context: Context, title: String, content: String, leftText: String, rightText: String, listener: View.OnClickListener) : super(context, R.style.MyDialog) {
        this.title = title
        this.content = content
        this.listener = listener
        this.leftStr = leftText
        this.rightStr = rightText
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_layout)

        val tvTitle = findViewById<TextView>(R.id.title)
        val tvContent = findViewById<TextView>(R.id.content)
        val leftText = findViewById<TextView>(R.id.left_text)
        val rightText = findViewById<TextView>(R.id.right_text)

        if (title == null) {
            tvTitle.visibility = View.GONE
        } else {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = title
        }
        tvContent.text = content
        if (leftStr != null && leftStr != "") {
            leftText.text = leftStr
        }

        if (rightStr != null && rightStr != "") {
            rightText.text = rightStr
        }

        leftText.setOnClickListener(listener)
        rightText.setOnClickListener(listener)

    }
}
