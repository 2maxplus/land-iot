package com.hyf.iot.ui.activity

import com.hyf.iot.R
import kotlinx.android.synthetic.main.layout_common_title.*
import android.view.View
import com.hyf.iot.App
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.NickNameContract
import com.hyf.iot.presenter.NickNamePresenter
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import kotlinx.android.synthetic.main.activity_modify_nickname.*


class ModifyNickNameActivity : BaseMvpActivity<NickNameContract.IPresenter>(), NickNameContract.IView {
    override fun onTokenExpired(msg: String) {
        showToast(msg)
        App.instance.removeAllActivity()
        newIntent<LoginActivity>()
        finish()
    }

    override fun registerPresenter() = NickNamePresenter::class.java

    override fun getLayoutId() = R.layout.activity_modify_nickname

    override fun initData() {
        val nickname = intent.getBundleExtra("bundle").getString("nickname")
        if (nickname.isNotEmpty())
            et_nickname.setText(nickname)
        et_nickname.setSelection(nickname.length)
    }

    override fun initView() {
        iv_back.setOnClickListener { onBackPressed() }
        tv_title.text = getString(R.string.modify_nickname)
        et_nickname.imgEnable = resources.getDrawable(R.drawable.icon_edit_delete)
        tv_operate.visibility = View.VISIBLE
        tv_operate.setOnClickListener {
            if (et_nickname.text.isEmpty()) {
                showToast(R.string.hint_nickname)
                return@setOnClickListener
            }
            getPresenter().modifyNickName(et_nickname.text.toString())
        }
    }

    override fun modifySuccess() {
        val intent = intent.putExtra("nickname", et_nickname.text.toString())
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun modifyError(errorMsg: String?) {
        if (errorMsg.isNullOrEmpty()) {
            showToast(R.string.net_error)
        } else {
            showToast(errorMsg)
        }
    }
}