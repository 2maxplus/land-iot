package com.hyf.iot.ui.activity

import com.hyf.iot.R
import com.hyf.iot.common.activity.BaseActivity
import kotlinx.android.synthetic.main.layout_common_title.*

/**
 * 农场详情
 *
 * */
class FarmDetailActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_farm_detail

    override fun initView() {
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = getString(R.string.farm_detail)
    }

    override fun initData() {

    }


}