package com.hyf.intelligence.kotlin.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.view.View
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.common.LoginUser
import com.hyf.intelligence.kotlin.common.activity.BaseMvpActivity
import com.hyf.intelligence.kotlin.contract.LoginContract
import com.hyf.intelligence.kotlin.presenter.LoginPresenter
import com.hyf.intelligence.kotlin.utils.showToast
import com.hyf.intelligence.kotlin.widget.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_login.*

/**
 *  1、继承BaseMvpActivity
 *  2、通过泛型告诉View层，当前Presenter使用的通讯契约
 *  3、实现自己的通讯契约
 */
class LoginActivity : BaseMvpActivity<LoginContract.IPresenter>(), LoginContract.IView {

    private val mLoadingDialog by lazy { LoadingDialog(this) }

    override fun getLayoutId() = R.layout.activity_login

    override fun registerPresenter() = LoginPresenter::class.java

    override fun initView() {
        btn_login.setOnClickListener { login() }
    }

    override fun initData() {
        if (LoginUser.name.isBlank()) {
            showLogin()
        } else {
            getPresenter().delayGoHomeTask()
        }
    }

    override fun loginSuccess() {
        mLoadingDialog.dismiss()
        goHome()
    }

    override fun loginError(errorMsg: String?) {
        mLoadingDialog.dismiss()
        if (errorMsg.isNullOrEmpty()) {
            showToast(R.string.net_error)
        } else {
            showToast(errorMsg)
        }
    }

    override fun goHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showLogin() {
        val alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(ll_login, alpha, scaleX, scaleY).setDuration(1000).start()
        ll_login.visibility = View.VISIBLE
    }


    private fun login() {
        if (et_user.text.isNullOrBlank()) {
            showToast(R.string.input_user)
            return
        }
        mLoadingDialog.show()
        getPresenter().login(et_user.text.trim().toString())
    }

}
