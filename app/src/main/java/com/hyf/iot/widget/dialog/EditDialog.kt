package com.hyf.iot.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.utils.StringUtils
import com.hyf.iot.utils.UIUtils
import com.hyf.iot.utils.showToast


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EditDialog : Dialog {

    private var title: String? = null
    private var content: String? = null
    private var leftStr: String? = null
    private var rightStr: String? = null
    private var maxTextNum = 20
    private var m_listener: OnPublicInputBoxClickListener? = null

    private var listener: View.OnClickListener? = null

    constructor(context: Activity?, content: String, listener: View.OnClickListener) : super(context, R.style.MyDialog) {
        this.content = content
        this.listener = listener
    }

    constructor(context: Activity?, title: String, content: String, listener: OnPublicInputBoxClickListener) : super(context, R.style.MyDialog) {
        this.title = title
        this.content = content
        this.m_listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_layout)

        //重置dialog 宽高
        val lp = window.attributes
        lp.width = UIUtils.getScreenWidth(context) * 4 / 5
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp

        val tvTitle = findViewById<TextView>(R.id.title)
        val etContent = findViewById<EditText>(R.id.content)
        val leftText = findViewById<TextView>(R.id.left_text)
        val rightText = findViewById<TextView>(R.id.right_text)

        if (title == null) {
            tvTitle.visibility = View.GONE
        } else {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = title
        }
        if (leftStr != null && leftStr != "") {
            leftText.text = leftStr
        }

        if (rightStr != null && rightStr != "") {
            rightText.text = rightStr
        }

        leftText.setOnClickListener {
            dismiss()
        }
        rightText.setOnClickListener {
            val text = etContent!!.text!!.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(text)) {
                context.showToast("你输入的内容为空")
                etContent.requestFocus()
                return@setOnClickListener
            } else if (StringUtils.calculateWeiboLength(text) > maxTextNum) {
                context.showToast("字数不能超过" + maxTextNum + "个")
                etContent.requestFocus()
                return@setOnClickListener
            }
            m_listener!!.inputTextContent(etContent)
            etContent.setText("")
            dismiss()
        }

        etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

    }

    interface OnPublicInputBoxClickListener {
        fun inputTextContent(editText: EditText)
    }
}
