package com.hyf.intelligence.kotlin.activity

import android.os.Bundle
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.common.activity.BaseActivity
import kotlinx.android.synthetic.main.layout_common_title.*

class AccountInfoActivity: BaseActivity() {
    override fun getLayoutId() = R.layout.activity_account_info


    override fun initSaveInstanceState(saveInstanceState: Bundle?) {

    }

    override fun initView() {
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = getString(R.string.my_account)
    }

    override fun initData() {

    }
}