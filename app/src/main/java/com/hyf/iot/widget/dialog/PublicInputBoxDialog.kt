package com.hyf.iot.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.utils.StringUtils
import com.hyf.iot.widget.ExtEditText

/**
 * 公用的输入框
 *
 */
class PublicInputBoxDialog : Dialog {
    /** 文本编辑框  */
    private var m_etContent: ExtEditText? = null
    /** 完成按扭  */
    private var m_tivComplete: TextView? = null
    /** 标题  */
    private var m_tvTitle: TextView? = null
    /** 编辑框中的可输入字数显示  */
    private var m_tvTextNum: TextView? = null
    /** 监听点击事件的回调接口  */
    private var m_listener: OnPublicInputBoxClickListener? = null
    private var mText: String? = null
    private var mTitle: String? = null
    private var maxTextNum = 198

    private var mcan: DialogInterface.OnCancelListener? = null

    internal var dispatchKeyEventPreIme: ExtEditText.OndispatchKeyEventPreIme = object : ExtEditText.OndispatchKeyEventPreIme {

        override fun dispatchKeyEventPreIme(event: KeyEvent) {
            if (KeyEvent.KEYCODE_BACK == event.keyCode) {
                dismiss()
                if (null != mcan) {
                    mcan!!.onCancel(this@PublicInputBoxDialog)
                }
            }
        }
    }

    /**
     * 有参的构造函数，
     *
     * @param context
     * 引用当前上下问 指定Dialog样式
     */
    constructor(context: Context, text: String,
                listener: OnPublicInputBoxClickListener) : super(context, R.style.MyDialog) {
        this.mText = text
        this.m_listener = listener
    }

    constructor(context: Context, text: String,
                listener: OnPublicInputBoxClickListener,
                mcancel: DialogInterface.OnCancelListener) : super(context, R.style.MyDialog) {
        this.mText = text
        this.m_listener = listener
        this.mcan = mcancel

    }

    /**
     * 创建Dialog
     */
    override fun onCreate(savedInstanceState: Bundle) {
        // 获取动态设置布局的实例对象
        val inflater = LayoutInflater.from(context)
        // 获取当前加载的布局实例对象
        val view = inflater.inflate(R.layout.public_input_box_dialog, null)
        // 引用布局
        setContentView(view)
        super.onCreate(savedInstanceState)
        showSubmitCmtDialog()
        m_tvTitle = view
                .findViewById<View>(R.id.tv_public_input_box_title) as TextView
        if (!TextUtils.isEmpty(mTitle)) {
            m_tvTitle!!.text = mTitle
        }
        m_tvTextNum = view
                .findViewById<View>(R.id.tv_public_input_box_text_num) as TextView
        m_etContent = view
                .findViewById<View>(R.id.et_public_input_box_content) as ExtEditText
        m_etContent!!.setDrawable(false)
        m_etContent!!.setDispatchKeyEventPreIme(dispatchKeyEventPreIme)
        if (!TextUtils.isEmpty(mText)) {
            m_etContent!!.setText(mText!! + "")
            m_etContent!!.setSelection(mText!!.length)
            val num = maxTextNum - StringUtils.calculateWeiboLength(m_etContent!!.text!!
                    .toString())
            m_tvTextNum!!.text = num.toString() + ""
        }
        m_tivComplete = view
                .findViewById(R.id.tiv_public_input_box_complete)
        m_tivComplete!!.setOnClickListener { v ->
            val text = m_etContent!!.text!!.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(text)) {
                //				SysAlertDialog.showAutoHideDialog(getContext(), "",
                //						"你输入的内容为空!", Toast.LENGTH_SHORT);
                m_etContent!!.requestFocus()
                return@setOnClickListener
            } else if (StringUtils.calculateWeiboLength(text) > maxTextNum) {
                //				SysAlertDialog.showAutoHideDialog(getContext(), "",
                //						"字数不能超过" + maxTextNum + "个！", Toast.LENGTH_SHORT);
                m_etContent!!.requestFocus()
                return@setOnClickListener
            }
            m_listener!!.inputTextContent(m_etContent!!)
            m_etContent!!.setText("")
            dismiss()
        }

        /**
         * 发表评论文本编辑器的监听
         */
        m_etContent!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                m_tvTextNum!!.text = (maxTextNum - StringUtils.calculateWeiboLength(s as String)).toString() + ""
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            // 用户输入时回调
            override fun afterTextChanged(s: Editable) {
                m_tivComplete!!.isEnabled = s.length > 0
                val i = m_etContent!!.selectionStart
                val j = m_etContent!!.selectionEnd
                // 调用判断输入字体的长度是否大于198(字母和数字2个字节代表一个字符)
                if (StringUtils
                                .calculateWeiboLength(m_etContent!!.text!!.toString()) > maxTextNum) {
                    //					SysAlertDialog.showAutoHideDialog(getContext(), "",
                    //							"字数超出限制", Toast.LENGTH_SHORT);
                    s.delete(i - 1, j)
                    m_etContent!!.setTextKeepState(s)
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        m_tivComplete!!.isEnabled = false
    }

    /**
     * 控制显示发表评论的Dialog
     */
    private fun showSubmitCmtDialog() {
        // 获取一个布局属性的实例对象
        val lp = window!!.attributes
        // 设置当前Dialog是位子居于底部
        lp.gravity = Gravity.BOTTOM
        // 设置当前Dialog宽度为最大
        lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT
        // 改变当前窗口设置的属性值
        this.onWindowAttributesChanged(lp)
        enableKeyboard()
    }

    /**
     * 开启键盘
     */
    fun enableKeyboard() {
        // 当前EditText初始话创建完毕后执行里面的代码
        findViewById<View>(R.id.et_public_input_box_content).post {
            // 第一次调用显示软键盘，不能在onCreate中直接调用
            val imm = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            // 获取焦点
            findViewById<View>(R.id.et_public_input_box_content).requestFocus()
        }
    }

    /**
     * 结束发表评论
     */
    fun endSubmitCommentDialog() {
        dismiss()
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see android.app.Dialog#dismiss()
	 */
    override fun dismiss() {
        super.dismiss()
        if (null != m_etContent) {
            m_etContent!!.setDispatchKeyEventPreIme(null)
            m_etContent = null
        }
        if (null != m_listener) {
            m_listener = null
        }
    }

    /**
     * 回调返回输入文字信息事件的接口
     *
     * @author johnny
     */
    interface OnPublicInputBoxClickListener {
        fun inputTextContent(editText: EditText)
    }

    /**
     * 设置hint
     *
     * @param hint
     */
    fun setHint(hint: String) {
        if (null != m_etContent)
            m_etContent!!.hint = hint
    }

    /**
     * 设置标题
     *
     * @param title
     */
    fun setTitle(title: String) {
        mTitle = title
        if (m_tvTitle != null && !TextUtils.isEmpty(title))
            m_tvTitle!!.text = title
    }

    /**
     * 清空文本
     *
     * @param text
     */
    fun setText(text: String) {
        mText = text
        if (null != m_etContent && !TextUtils.isEmpty(text))
            m_etContent!!.setText(text)
    }

    fun setMaxTextNum(maxTextNum: Int) {
        this.maxTextNum = maxTextNum
    }

}
