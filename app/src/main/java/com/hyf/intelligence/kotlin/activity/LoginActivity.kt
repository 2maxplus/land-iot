package com.hyf.intelligence.kotlin.activity

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.common.LoginUser
import com.hyf.intelligence.kotlin.common.activity.BaseMvpActivity
import com.hyf.intelligence.kotlin.contract.LoginContract
import com.hyf.intelligence.kotlin.presenter.LoginPresenter
import com.hyf.intelligence.kotlin.utils.StringUtils
import com.hyf.intelligence.kotlin.utils.newIntent
import com.hyf.intelligence.kotlin.utils.showToast
import com.hyf.intelligence.kotlin.widget.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_login.*

/**
 *  1、继承BaseMvpActivity
 *  2、通过泛型告诉View层，当前Presenter使用的通讯契约
 *  3、实现自己的通讯契约
 */
class LoginActivity : BaseMvpActivity<LoginContract.IPresenter>(), LoginContract.IView {

    private var loginId: String = ""
    private val mLoadingDialog by lazy { LoadingDialog(this) }
    private val timer = object : CountDownTimer(60000, 1000) {
        override fun onFinish() {
            tv_get_code.isEnabled = true
            tv_get_code.text = getString(R.string.get_code)
        }
        override fun onTick(millisUntilFinished: Long) {
            tv_get_code.isEnabled = false
            tv_get_code.text = "已发送  (${millisUntilFinished / 1000})"
        }
    }

    private fun setTimeDown() {
        tv_get_code.isEnabled = false
        timer.start()
    }

    override fun getLayoutId() = R.layout.activity_login
    override fun registerPresenter() = LoginPresenter::class.java

    override fun initView() {
        Glide.with(this)
                .load(R.drawable.ic_launcher)
                .apply(RequestOptions().transform(CircleCrop()))
                .into(iv_login)

        btn_login.setOnClickListener { login() }
        tv_get_code.setOnClickListener {
            //判断手机号
            if (et_user.text.isNullOrBlank()) {
                showToast(R.string.input_user)
                et_user.requestFocus()
                return@setOnClickListener
            }

            if (!StringUtils.isPhone(et_user.text.toString())) {
                showToast("请输入正确的手机号")
                et_user.requestFocus()
                return@setOnClickListener
            }
            //获取验证码
            getPresenter().getCode(et_user.text.trim().toString())

        }
        et_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btn_login.isEnabled = s!!.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun initData() {
        if (LoginUser.token.isBlank()) {
            showLogin()
        } else {
            getPresenter().delayGoHomeTask()
        }
    }

    override fun loginSuccess() {
        mLoadingDialog.dismiss()
        showToast(R.string.login_success)
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
        newIntent<MainActivity>()
        finish()
    }

    private fun showLogin() {
//        val alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
//        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f)
//        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f)
//        ObjectAnimator.ofPropertyValuesHolder(ll_login, alpha, scaleX, scaleY).setDuration(1000).start()
        ll_login.visibility = View.VISIBLE
    }


    private fun login() {
        if (et_user.text.isNullOrBlank()) {
            showToast(R.string.input_user)
            return
        }
        mLoadingDialog.show()
        getPresenter().login(loginId, et_code.text.trim().toString())
    }

    override fun getCodeSuccess(loginId: String) {
        showToast("验证码发送成功")
        this.loginId = loginId
        et_code.requestFocus()
        setTimeDown()
    }

    override fun getCodeError(errorMsg: String?) {
        tv_get_code.isEnabled = true
        tv_get_code.text = getString(R.string.get_code)
        if (errorMsg.isNullOrEmpty()) {
            showToast(R.string.net_error)
        } else {
            showToast(errorMsg)
        }
    }

}
