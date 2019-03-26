package com.hyf.intelligence.kotlin.fragment.pumb

import android.view.View
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.adapter.home.BengFragmentAdapter
import com.hyf.intelligence.kotlin.common.fragment.BaseMvpFragment
import com.hyf.intelligence.kotlin.contract.PumpRoomContract
import com.hyf.intelligence.kotlin.domain.pumb.PumpControlStations
import com.hyf.intelligence.kotlin.presenter.PumpRoomPresenter
import com.hyf.intelligence.kotlin.widget.PageStateLayout
import kotlinx.android.synthetic.main.layout_common_page_state.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_common_viewpager.*



class PumbRoomFragment: BaseMvpFragment<PumpRoomContract.IPresenter>(),PumpRoomContract.IView {

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
            setOnRefreshListener { getPresenter().getPumpInfo() }
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

    fun getonScroll(isEnable: Boolean) {
        refresh_layout.isEnabled = isEnable
    }
}