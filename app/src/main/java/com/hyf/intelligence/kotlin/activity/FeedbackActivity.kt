package com.hyf.intelligence.kotlin.activity

import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.common.activity.BaseMvpActivity
import com.hyf.intelligence.kotlin.contract.FeedbackContract
import com.hyf.intelligence.kotlin.presenter.FeedbackPresenter
import com.hyf.intelligence.kotlin.utils.showToast
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.layout_common_title.*

class FeedbackActivity: BaseMvpActivity<FeedbackContract.IPresenter>(),FeedbackContract.IView {
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