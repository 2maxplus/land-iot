package com.hyf.iot.ui.activity

import android.annotation.SuppressLint
import com.hyf.iot.BuildConfig
import com.hyf.iot.R
import com.hyf.iot.common.HTTP_API_DOWNLOAD_RELEASE
import com.hyf.iot.common.activity.BaseActivity
import com.hyf.iot.protocol.http.CustomUpdateParser
import com.xuexiang.xupdate.XUpdate
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.layout_common_title.*


class AboutUsActivity : BaseActivity() {

    private var mTitle: String? = "关于我们"

    override fun getLayoutId() = R.layout.activity_about_us

    @SuppressLint("SetTextI18n")
    override fun initView() {
        tv_title.text = mTitle
        iv_back.setOnClickListener { onBack() }

        tv_version_text.text = "当前版本   " + BuildConfig.VERSION_NAME
        tv_version_update.setOnClickListener {
            XUpdate.newBuild(this)
                    .topResId(R.drawable.xupdate_bg_app_top)
                    .themeColor(resources.getColor(R.color.colorPrimary))
                    .updateUrl(HTTP_API_DOWNLOAD_RELEASE)
                    .updateParser(CustomUpdateParser()) //设置自定义的版本更新解析器
                    .update()
        }

        tv_telephone.setOnClickListener {
            //            StringUtils.callPhone(this,getString(R.string.telephone_number))
        }
    }

    override fun initData() {

    }

}