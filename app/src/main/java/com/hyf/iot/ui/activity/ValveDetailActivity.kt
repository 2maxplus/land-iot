package com.hyf.iot.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.adapter.LegendAdapter
import com.hyf.iot.adapter.device.DeviceSensorAdapter
import com.hyf.iot.adapter.device.ValveListAdapter
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.DeviceDetailContract
import com.hyf.iot.domain.LegendBean
import com.hyf.iot.domain.device.DeviceInfo
import com.hyf.iot.domain.device.ValveUseTime
import com.hyf.iot.presenter.DeviceDetailPresenter
import com.hyf.iot.utils.TimeUtils
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import kotlinx.android.synthetic.main.activity_valve_detail.*
import kotlinx.android.synthetic.main.item_device.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_recycler_view.*

class ValveDetailActivity : BaseMvpActivity<DeviceDetailContract.IPresenter>(), DeviceDetailContract.IView {
    override fun onTokenExpired(msg: String) {
        showToast(msg)
        App.instance.removeAllActivity()
        newIntent<LoginActivity>()
        finish()
    }

    override fun registerPresenter() = DeviceDetailPresenter::class.java

    private var id: String = ""

    private val arrColor = arrayListOf(R.color.plan1, R.color.plan2, R.color.plan3, R.color.plan4)

    override fun getLayoutId(): Int = R.layout.activity_valve_detail

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        id = intent.getBundleExtra("bundle").getString("id")
    }

    override fun initView() {
        iv_back.setOnClickListener { finish() }
    }

    override fun showPage(data: MutableList<ValveUseTime>) {
        for (itemDate in data) {
            for (itemUseTime in itemDate.useTime) {
                for (item in itemUseTime.startEnds) {
                    val diff = TimeUtils.dateDiff(item.start, item.end)
                    item.interval = diff.toString()
                    Log.e("diff", diff)
                    val startH = TimeUtils.getHour(item.start).toFloat()
                    val startM = TimeUtils.getMin(item.start).toFloat()
                    item.start = (startH + (startM.div(60))).toString()

                    val endH = TimeUtils.getHour(item.end).toFloat()
                    val endM = TimeUtils.getMin(item.end).toFloat()
                    item.end = (endH + (endM.div(60))).toString()
                }
            }
        }
        horizontalChartView.setDatas(data as ArrayList<ValveUseTime>?)
    }

    override fun errorPage(msg: String?) {
        showToast(msg!!)
    }

    @SuppressLint("SetTextI18n")
    override fun showDetailPage(deviceItem: DeviceInfo) {
        if (!deviceItem.sensor_OtherInfos.isNullOrEmpty()) {
            val sAdapter = DeviceSensorAdapter(this, deviceItem.sensor_OtherInfos)
            recycler_view_sensor.apply {
                this!!.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
                adapter = sAdapter
            }
        }
//        tv_device_name?.text = "${deviceInfos.name}:"
        if (deviceItem.sensor_SignalInfo != null)
            signalView.setSignalValue(deviceItem.sensor_SignalInfo.value)
        tv_title.text = deviceItem.name
        tv_device_name.visibility = View.GONE
        tv_device_no.text = deviceItem.number

        tv_state.text = deviceItem.stateString
        when (deviceItem.state) {
            0, 1 -> {
                tv_state.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gray_oval, 0, 0, 0)
            }
            2 -> {
                tv_state.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green_oval, 0, 0, 0)
            }
            3 -> {
                tv_state.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.red_oval, 0, 0, 0)
            }
        }
        if(deviceItem.sensor_VoltageInfo != null) {
            tv_battery_percent.text = "${(deviceItem.sensor_VoltageInfo.value).toInt()}%"
            battery.power = deviceItem.sensor_VoltageInfo.value / 100
        }
        if (deviceItem.sensor_ValveInfos != null) {
            val mAdapter = ValveListAdapter(this, deviceItem.sensor_ValveInfos)
            mAdapter.setGetOunts(object : ValveListAdapter.GetCounts {
                override fun adds() {
                }
                override fun subs() {
                }
            })
            recycler_view.apply {
                layoutManager = GridLayoutManager(this@ValveDetailActivity, 2)
                adapter = mAdapter
            }

            val legendList = ArrayList<LegendBean>()
            for (i in 0 until deviceItem.sensor_ValveInfos.size) {
                legendList.add(LegendBean(arrColor[i], deviceItem.sensor_ValveInfos[i].name))
            }
            legend_recycler_view.apply {
                layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                adapter = LegendAdapter(this@ValveDetailActivity, legendList)
            }
        }
    }

    override fun initData() {
        getPresenter().getValveUseTimesById(id)
        getPresenter().getDeviceDetailById(id)
    }

}