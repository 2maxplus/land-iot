package com.hyf.iot.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.hyf.iot.R
import com.hyf.iot.common.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.layout_common_title.*


class MessageActivity : BaseActivity() {

    companion object {
        private const val CONTENT = "content"
        private const val TITLE = "title"

        fun startActivity(context: Context, content: String, title: String = "") {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(CONTENT, content)
            intent.putExtra(TITLE, title)
            context.startActivity(intent)
        }
    }

    private var mTitle: String? = null
    private var mContent: String? = null

    override fun getLayoutId() = R.layout.activity_message

    override fun initSaveInstanceState(savedInstanceState: Bundle?) {
        mTitle = intent.getStringExtra(TITLE)
        mContent = intent.getStringExtra(CONTENT)
    }

    override fun initView() {
        iv_back.setOnClickListener { onBack() }

    }

    override fun initData() {
        if (!TextUtils.isEmpty(mTitle)) {
            tv_title.text = mTitle
        }
        if (!TextUtils.isEmpty(mContent)) {
            tv_content.text = mContent
        }

    }
}