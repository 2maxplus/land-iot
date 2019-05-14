package com.hyf.iot.activity

import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.FeedbackContract
import com.hyf.iot.presenter.FeedbackPresenter
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.layout_common_title.*

class FeedbackActivity: BaseMvpActivity<FeedbackContract.IPresenter>(),FeedbackContract.IView {
    override fun onTokenExpired(msg: String) {
        showToast(msg)
        App.instance.removeAllActivity()
        newIntent<LoginActivity>()
        finish()
    }

    override fun registerPresenter() = FeedbackPresenter::class.java

    override fun feedbackSuccess(msg: String) {
        showToast(msg)
        finish()
    }

    override fun feedbackError(errorMsg: String?) {
        if(errorMsg.isNullOrEmpty()){
            showToast(R.string.net_error)
        }else{
            showToast(errorMsg)
        }
    }

    override fun getLayoutId() = R.layout.activity_feedback


    override fun initView() {
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = getString(R.string.help_feedback)
        btn_submit.setOnClickListener {
            if(et_content.text.isEmpty()){
                showToast(R.string.hint_edit_feedback)
                return@setOnClickListener
            }
            getPresenter().feedbackAdd(et_content.text.toString())
        }
    }

}