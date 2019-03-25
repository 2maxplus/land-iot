package com.hyf.intelligence.kotlin.fragment.home

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.act.AccountInfoActivity
import com.hyf.intelligence.kotlin.act.LoginActivity
import com.hyf.intelligence.kotlin.common.fragment.BaseMvpFragment
import com.hyf.intelligence.kotlin.contract.MyContract
import com.hyf.intelligence.kotlin.domain.User
import com.hyf.intelligence.kotlin.presenter.MyPresenter
import com.hyf.intelligence.kotlin.utils.newIntent
import com.hyf.intelligence.kotlin.widget.dialog.NormalMsgDialog
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.mine_layout.*

class MyFragment: BaseMvpFragment<MyContract.IPresenter>(), MyContract.IView {
    override fun getLayoutId(): Int = R.layout.mine_layout

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
    }

    override fun initData() {
        getPresenter().getUserInfo()
    }

    override fun logoutSuccess() {
        goLogin()
    }

    private fun goLogin() {
        startActivity(Intent(activity, LoginActivity::class.java))
        activity!!.finish()
    }

    override fun showUserInfo(user: User) {
//        ImageLoader.load(activity!!, user.avatar_url, iv_header, ImageLoader.getCircleRequest())
//        tv_name.text = user.login
//        tv_location.text = user.location
//        tv_company.text = user.company
    }


}