package com.hyf.intelligence.iot.fragment.home

import android.content.DialogInterface
import android.view.View
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.activity.*
import com.hyf.intelligence.iot.common.fragment.BaseMvpFragment
import com.hyf.intelligence.iot.contract.MyContract
import com.hyf.intelligence.iot.domain.User
import com.hyf.intelligence.iot.presenter.MyPresenter
import com.hyf.intelligence.iot.utils.newIntent
import com.hyf.intelligence.iot.widget.dialog.NormalMsgDialog
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.fragment_mine.*

class MyFragment: BaseMvpFragment<MyContract.IPresenter>(), MyContract.IView {
    override fun onTokenExpired(msg: String) {
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine

    private val mLogoutDialog by lazy {
        NormalMsgDialog(activity!!)
                .setMessage(R.string.logout_user)
                .setLeftButtonInfo(R.string.cancel)
                .setRightButtonInfo(R.string.enter, DialogInterface.OnClickListener { _, _ ->
                    getPresenter().logout()
                })
    }

    override fun registerPresenter() = MyPresenter::class.java

    override fun initView() {
        iv_back.visibility = View.GONE
        tv_title.text = "我的"
        tv_account.setOnClickListener { activity?.newIntent<AccountInfoActivity>() }
        tv_help_feedback.setOnClickListener { activity?.newIntent<FeedbackActivity>() }
        tv_about_us.setOnClickListener { MessageActivity.startActivity(context!!, "关于我们","关于我们") }
        tv_message.setOnClickListener { MessageActivity.startActivity(context!!, "我的消息","我的消息") }
    }

    override fun initData() {
        getPresenter().getUserInfo()
    }

    override fun logoutSuccess() {
        goLogin()
    }

    private fun goLogin() {
        activity?.newIntent<LoginActivity>()
        activity!!.finish()
    }

    override fun showUserInfo(user: User) {
//        ImageLoader.load(activity!!, user.avatar_url, iv_header, ImageLoader.getCircleRequest())
//        tv_name.text = user.login
//        tv_location.text = user.location
//        tv_company.text = user.company
    }


}