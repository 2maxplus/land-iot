package com.hyf.iot.ui.fragment.pumb

import android.content.Intent
import android.view.View
import com.hyf.iot.R
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.adapter.home.BengFragmentAdapter
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.PumpRoomContract
import com.hyf.iot.domain.pumb.PumpControlStations
import com.hyf.iot.presenter.PumpRoomPresenter
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

    override fun errorPage(msg: String?) {
        page_layout.setPage(PageStateLayout.PageState.STATE_ERROR)
    }

}