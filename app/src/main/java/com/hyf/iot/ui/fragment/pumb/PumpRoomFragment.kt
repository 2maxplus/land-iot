package com.hyf.iot.ui.fragment.pumb

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.hyf.iot.R
import com.hyf.iot.adapter.home.PumpRoomFragmentAdapter
import com.hyf.iot.common.Constant
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.PumpRoomContract
import com.hyf.iot.domain.pumb.PumpRoom
import com.hyf.iot.presenter.PumpRoomPresenter
import com.hyf.iot.ui.activity.FarmListActivity
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.PageStateLayout
import kotlinx.android.synthetic.main.layout_common_page_state.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_common_viewpager.*

class PumpRoomFragment : BaseMvpFragment<PumpRoomContract.IPresenter>(), PumpRoomContract.IView {

    private var isRefresh = false
    override fun onTokenExpired(msg: String) {
        activity?.showToast(msg)
        activity?.newIntent<LoginActivity>()
        activity?.finish()
    }

    override fun registerPresenter() = PumpRoomPresenter::class.java

    override fun getLayoutId(): Int = R.layout.pump_room_layout

    private val mAdapter by lazy { PumpRoomFragmentAdapter(childFragmentManager, mutableListOf()) }

    override fun initView() {
        tv_title.textSize = 15f
        tv_title.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_exchange,0)
        tv_title.setOnClickListener{
            val intent = Intent(context, FarmListActivity::class.java)
            startActivityForResult(intent, Constant.RequestKey.ON_SUCCESS)
        }
        iv_back.visibility = View.INVISIBLE
        tv_operate.visibility = View.VISIBLE
        tv_operate.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_refresh, 0, 0, 0)
        tv_operate.setOnClickListener {
            onReload()
        }
        page_layout.apply {
            setContentView(View.inflate(activity, R.layout.layout_common_viewpager, null))
            setOnPageErrorClickListener { onReload() }
        }
        refresh_layout.apply {
            setColorSchemeResources(R.color.colorBlue)
            setOnRefreshListener {
                onReload()
            }
        }
        viewPager.apply {
            adapter = mAdapter
            currentItem = 0
        }
    }


//    override fun initData() {
//        getPresenter().getPumpInfo(LoginUser.farmId)
//    }

    override fun onResume() {
        super.onResume()
//        tv_title.text = LoginUser.farmName
        onReload()
    }

    private fun onReload() {
        isRefresh = true
        page_layout.setPage(PageStateLayout.PageState.STATE_LOADING)
        getPresenter().getPumpInfo(LoginUser.farmId)
//        initData()
//        activity?.sendBroadcast(Intent(FrequencyConverterFragment.INTENT_ACTION_REFRESH))
    }

    @SuppressLint("SetTextI18n")
    override fun showPage(data: PumpRoom) {
        tv_title.text = "${LoginUser.farmName}\n${data.pumpRoomInfo.name}"
//        refresh_layout.finishRefresh()
        refresh_layout.isRefreshing = false
        if (data == null || data.pumpRoomInfo == null) {
            page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        } else {
            page_layout.setPage(PageStateLayout.PageState.STATE_SUCCEED)
            viewPager.offscreenPageLimit = data.frequencyConverterCabinetInfos.size
            mAdapter.fragmenList.clear()
            mAdapter.fragmenList.addAll(data.frequencyConverterCabinetInfos)
            mAdapter.isRefresh(isRefresh)
            mAdapter.notifyDataSetChanged()
        }
        if (childFragmentManager.fragments.size > 0) {
            ((childFragmentManager.fragments[viewPager.currentItem]) as FrequencyConverterFragment).initData()
        }
    }

    override fun errorPage(msg: String?) {
        activity?.showToast(msg!!)
        page_layout.setPage(PageStateLayout.PageState.STATE_ERROR)
    }


    fun getOnScroll(enable: Boolean) {
        refresh_layout.isEnabled = enable
    }
}