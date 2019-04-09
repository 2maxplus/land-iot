package com.hyf.intelligence.iot.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import android.support.v4.app.ActivityCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.common.LoginUser
import com.hyf.intelligence.iot.common.activity.BaseMvpActivity
import com.hyf.intelligence.iot.contract.LoginContract
import com.hyf.intelligence.iot.presenter.LoginPresenter
import com.hyf.intelligence.iot.utils.StringUtils
import com.hyf.intelligence.iot.utils.newIntent
import com.hyf.intelligence.iot.utils.showToast
import com.hyf.intelligence.iot.widget.dialog.LoadingDialog
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_login.*
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


/**
 *  1、继承BaseMvpActivity
 *  2、通过泛型告诉View层，当前Presenter使用的通讯契约
 *  3、实现自己的通讯契约
 */
class LoginActivity : BaseMvpActivity<LoginContract.IPresenter>(), LoginContract.IView {
    private val BAIDU_READ_PHONE_STATE = 100
    override fun onTokenExpired(msg: String) {
    }

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
        transparencyBar(this)
//        Glide.with(this)
//                .load(R.drawable.login_bg)
//                .apply(RequestOptions.bitmapTransform(BlurTransformation(5,3)))
//                .into(object : SimpleTarget<Drawable>() {
//                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                        rl_login.background = resource
//                    }
//                })
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

        if (Build.VERSION.SDK_INT >= 23) {
            showPermissions()
        }
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
        Glide.with(this)
                .load(R.drawable.login_bg)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 3)))
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        rl_login.background = resource
                    }
                })
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

    fun showPermissions() {
        if (ActivityCompat.checkSelfPermission(
                        this,
//                        Manifest.permission_group.LOCATION
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission_group.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            this.let {
                ActivityCompat.requestPermissions(
                        it,
                        arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        ),
                        BAIDU_READ_PHONE_STATE
                )
            }
        }
    }

    //Android6.0申请权限的回调方法
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            BAIDU_READ_PHONE_STATE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // 没有获取到权限，做特殊处理
                showToast("没有获取到权限")
            }
        }
    }

}
