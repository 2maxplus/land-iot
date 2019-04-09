package com.hyf.intelligence.iot.fragment.pumb

import android.content.Intent
import android.view.View
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.activity.LoginActivity
import com.hyf.intelligence.iot.adapter.home.BengFragmentAdapter
import com.hyf.intelligence.iot.common.fragment.BaseMvpFragment
import com.hyf.intelligence.iot.contract.PumpRoomContract
import com.hyf.intelligence.iot.domain.pumb.PumpControlStations
import com.hyf.intelligence.iot.presenter.PumpRoomPresenter
import com.hyf.intelligence.iot.utils.newIntent
import com.hyf.intelligence.iot.utils.showToast
import com.hyf.intelligence.iot.widget.PageStateLayout
import kotlinx.android.synthetic.main.layout_common_page_state.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_common_viewpager.*



class PumbRoomFragment: BaseMvpFragment<PumpRoomContract.IPresenter>(),PumpRoomContract.IView{


    override fun onTokenExpired(msg: String) {
        activity?.showToast(msg)
        activity?.newIntent<LoginActivity>()
        activity?.finish()
    }

    override fun registerPresenter() = PumpRoomPresenter::class.java

    override fun getLayoutId(): Int = R.layout.pump_room_layout

    private val mAdapter by lazy { BengFragmentAdapter(childFragmentManager, mutableListOf()) }


    override fun initView() {
        iv_back.visibility = View.GONE

        page_layout.apply {
            setContentView(View.inflate(activity, R.layout.layout_common_viewpager, null))
            setOnPageErrorClickListener { onReload() }
        }
        refresh_layout.apply {
            setColorSchemeResources(R.color.colorBlue)
            setOnRefreshListener {
                getPresenter().getPumpInfo()
                activity?.sendBroadcast(Intent(PumpItemFragment.INTENT_ACTION_REFRESH))
            }
        }
        viewPager.apply {
            adapter = mAdapter
            currentItem = 0
        }
    }

    override fun initData() {
        getPresenter().getPumpInfo()
    }

    private fun onReload() {
        page_layout.setPage(PageStateLayout.PageState.STATE_LOADING)
        getPresenter().getPumpInfo()
    }

    override fun showPage(data: PumpControlStations) {
        tv_title.text = data.name

        refresh_layout.isRefreshing = false
        if (data == null) {
            page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        } else {
            page_layout.setPage(PageStateLayout.PageState.STATE_SUCCEED)
            mAdapter.fragmenList.clear()
            mAdapter.fragmenList.addAll(data.pumpControlStations)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun errorPage(t: Throwable) {
        page_layout.setPage(PageStateLayout.PageState.STATE_ERROR)
    }

}