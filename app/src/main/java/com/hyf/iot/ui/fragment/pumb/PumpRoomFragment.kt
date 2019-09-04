package com.hyf.iot.ui.fragment.pumb

import android.view.View
import com.hyf.iot.R
import com.hyf.iot.adapter.home.PumpRoomFragmentAdapter
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.PumpRoomContract
import com.hyf.iot.domain.pumb.PumpRoom
import com.hyf.iot.presenter.PumpRoomPresenter
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.PageStateLayout
import kotlinx.android.synthetic.main.layout_common_page_state.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_common_viewpager.*

class PumpRoomFragment: BaseMvpFragment<PumpRoomContract.IPresenter>(),PumpRoomContract.IView{

    override fun onTokenExpired(msg: String) {
        activity?.showToast(msg)
        activity?.newIntent<LoginActivity>()
        activity?.finish()
    }

    override fun registerPresenter() = PumpRoomPresenter::class.java

    override fun getLayoutId(): Int = R.layout.pump_room_layout

    private val mAdapter by lazy { PumpRoomFragmentAdapter(childFragmentManager, mutableListOf()) }


    override fun initView() {
        iv_back.visibility = View.GONE
        tv_operate.visibility = View.VISIBLE
        tv_operate.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_refresh,0,0,0)
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

    override fun initData() {
        getPresenter().getPumpInfo(LoginUser.farmId)
    }

    private fun onReload() {
        page_layout.setPage(PageStateLayout.PageState.STATE_LOADING)
        initData()
//        activity?.sendBroadcast(Intent(FrequencyConverterFragment.INTENT_ACTION_REFRESH))
    }

    override fun showPage(data: PumpRoom) {
        tv_title.text = data.pumpRoomInfo.name

        refresh_layout.isRefreshing = false
        if (data == null || data.pumpRoomInfo == null) {
            page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        } else {
            page_layout.setPage(PageStateLayout.PageState.STATE_SUCCEED)
            mAdapter.fragmenList.clear()
            mAdapter.fragmenList.addAll(data.frequencyConverterCabinetInfos)
            mAdapter.notifyDataSetChanged()
        }
        if(childFragmentManager.fragments.size > 0)
        ((childFragmentManager.fragments[viewPager.currentItem]) as FrequencyConverterFragment).initData()
    }

    override fun errorPage(msg: String?) {
        activity?.showToast(msg!!)
        page_layout.setPage(PageStateLayout.PageState.STATE_ERROR)
    }



}