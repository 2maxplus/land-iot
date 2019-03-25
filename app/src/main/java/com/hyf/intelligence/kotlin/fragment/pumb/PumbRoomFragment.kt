package com.hyf.intelligence.kotlin.fragment.pumb

import android.view.View
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.adapter.home.BengFragmentAdapter
import com.hyf.intelligence.kotlin.common.fragment.BaseMvpFragment
import com.hyf.intelligence.kotlin.contract.PumpRoomContract
import com.hyf.intelligence.kotlin.domain.pumb.PumpControlStations
import com.hyf.intelligence.kotlin.presenter.PumpRoomPresenter
import kotlinx.android.synthetic.main.pump_room_layout.*
import kotlinx.android.synthetic.main.layout_common_title.*

class PumbRoomFragment: BaseMvpFragment<PumpRoomContract.IPresenter>(),PumpRoomContract.IView {

    override fun registerPresenter() = PumpRoomPresenter::class.java

    override fun getLayoutId(): Int  = R.layout.pump_room_layout

    private val mAdapter by lazy { BengFragmentAdapter(childFragmentManager, mutableListOf()) }


    override fun initView() {
        iv_back.visibility = View.GONE
        viewPager.apply {
            adapter = mAdapter
            currentItem = 0
        }
    }

    override fun initData() {
        getPresenter().getPumpInfo()
    }

    override fun showPage(data: PumpControlStations) {
        tv_title.text = data.name
        mAdapter.fragmenList.clear()
        mAdapter.fragmenList.addAll(data.pumpControlStations)
        mAdapter.notifyDataSetChanged()
    }

    override fun errorPage(t: Throwable) {

    }

}