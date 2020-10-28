package com.hyf.iot.ui.fragment.main

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.adapter.device.PumpStationExpandableListViewAdapter
import com.hyf.iot.common.Constant
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.PumpStationContract
import com.hyf.iot.domain.pumb.PumpStation
import com.hyf.iot.presenter.PumpStationPresenter
import com.hyf.iot.ui.activity.FarmListActivity
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.BatteryView
import com.hyf.iot.widget.PageStateLayout
import com.hyf.iot.widget.PinnedHeaderExpandableListView
import com.hyf.iot.widget.SignalView
import com.hyf.iot.widget.dialog.CountDownDialog
import com.hyf.iot.widget.dialog.CountDownDialog.CountDownFinishListener
import kotlinx.android.synthetic.main.layout_common_page_state.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_expandable_listview.*


class PumpStationFragment : BaseMvpFragment<PumpStationContract.IPresenter>(), PumpStationContract.IView
        ,PinnedHeaderExpandableListView.OnHeaderUpdateListener{

    private val mAdapter by lazy { PumpStationExpandableListViewAdapter(activity, mutableListOf()) }

    override fun registerPresenter() = PumpStationPresenter::class.java

    override fun getLayoutId(): Int = R.layout.fragment_pump_station

    private val mLoadingDialog by lazy {
        CountDownDialog(context!!, object : CountDownFinishListener {
            override fun onFinish() {
                onReload()
            }
        })
    }

    override fun initView() {
        iv_back.visibility = View.INVISIBLE
        tv_title.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_exchange,0)
        tv_title.setOnClickListener{
            val intent = Intent(context, FarmListActivity::class.java)
            startActivityForResult(intent, Constant.RequestKey.ON_SUCCESS)
        }
        page_layout.apply {
            setContentView(View.inflate(activity, R.layout.layout_expandable_listview, null))
            setOnPageErrorClickListener { onReload() }
        }

        refresh_layout.apply {
            setColorSchemeResources(R.color.colorBlue)
            setOnRefreshListener { onReload() }
        }

        mAdapter.setCountDownDialog(mLoadingDialog)

        expandableListView.apply {
            setAdapter(mAdapter)
            setGroupIndicator(null)
            //只展开一个组
            setOnGroupExpandListener {
                val count = mAdapter.groupCount
                index = it
                for (i in 0 until count) {
                    if (i != it) {
                        collapseGroup(i)
                    }
                }
            }
            //点击更新展开的viewgroup在顶部
//            setOnHeaderUpdateListener(this@PumpStationFragment)
            setOnScrollListener(object : AbsListView.OnScrollListener{
                override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                }
                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                    if (view != null) {
                        getPositionAndOffset()
                    }
                }
            })
        }

    }

    private var lastOffset = 0
    private var lastPosition = 0
    private var index = 0

    /**
     * 记录View当前位置
     */
    private fun getPositionAndOffset() {
        //获取可视的第一个view
        val topView = expandableListView.getChildAt(index)
        if (topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.top  // this.lastOffset = topView.top
            //得到该View的数组位置
            lastPosition = expandableListView.getPositionForView(topView)
        }
    }

    /**
     * 让View滚动到指定位置
     */
    private fun scrollToPosition() {
        if (lastPosition >= 0) {
            expandableListView.smoothScrollToPositionFromTop(lastPosition,lastOffset)
        }
    }

//    override fun initData() {
//        getPresenter().getPumpStationListByFarmId(LoginUser.farmId)
//    }

    override fun onResume() {
        super.onResume()
        tv_title.text = LoginUser.farmName
        onReload()
    }

    override fun onTokenExpired(msg: String) {
        activity?.showToast(msg)
        activity?.newIntent<LoginActivity>()
        activity?.finish()
    }

    private fun onReload() {
        page_layout.setPage(PageStateLayout.PageState.STATE_LOADING)
        getPresenter().getPumpStationListByFarmId(LoginUser.farmId)
    }

    override fun showPage(data: MutableList<PumpStation>) {
        refresh_layout.isRefreshing = false
        if (data.size == 0) {
            page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        } else {
            page_layout.setPage(PageStateLayout.PageState.STATE_SUCCEED)
            mAdapter.list.clear()
            mAdapter.list.addAll(data)
            mAdapter.notifyDataSetChanged()
            if(index >= data.size){
                index = 0
            }
            expandableListView.expandGroup(index)
        }
        scrollToPosition()
    }

    override fun errorPage(msg: String?) {
        activity?.showToast(msg!!)
        page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
    }

    @SuppressLint("SetTextI18n")
    override fun updatePinnedHeader(headerView: View, firstVisibleGroupPos: Int) {
        if(firstVisibleGroupPos >= 0) {
            val firstVisibleGroup = mAdapter.getGroup(firstVisibleGroupPos)

            val deviceName = headerView.findViewById<TextView>(R.id.tv_device_name)
            val deviceState = headerView.findViewById<TextView>(R.id.tv_state)
            val signalView = headerView.findViewById<SignalView>(R.id.signalView)
            val batteryPercent = headerView.findViewById<TextView>(R.id.tv_battery_percent)
            val battery = headerView.findViewById<BatteryView>(R.id.battery)
            val deviceNo = headerView.findViewById<TextView>(R.id.tv_device_no)
            val temperature = headerView.findViewById<TextView>(R.id.tv_temperature)

            deviceName?.text = firstVisibleGroup.name
            deviceNo?.text = firstVisibleGroup.number

            deviceState?.text = firstVisibleGroup.stateString
            when (firstVisibleGroup.state) {
                0, 1 -> {
                    deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gray_oval, 0, 0, 0)
                }
                2 -> {
                    deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green_oval, 0, 0, 0)
                }
                3 -> {
                    deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.red_oval, 0, 0, 0)
                }
            }

            signalView?.setSignalValue(firstVisibleGroup.signalIntensity)

            batteryPercent?.text = "${firstVisibleGroup.cellVoltage}%"
            battery?.power = firstVisibleGroup.cellVoltage.toFloat() / 100

            temperature?.text = "温度:${firstVisibleGroup.temperature}℃"
        }
    }

    @SuppressLint("InflateParams")
    override fun getPinnedHeader(): View {
        val headerView = layoutInflater.inflate(R.layout.item_pumpstation_group, null) as ViewGroup
        headerView.layoutParams = AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT)
        return headerView
    }

}