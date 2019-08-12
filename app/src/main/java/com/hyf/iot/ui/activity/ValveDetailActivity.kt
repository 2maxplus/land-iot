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
import com.hyf.iot.adapter.home.DeviceSoilSensorAdapter
import com.hyf.iot.adapter.home.ValveListAdapter
import com.hyf.iot.common.activity.BaseMvpActivity
import com.hyf.iot.contract.DeviceDetailContract
import com.hyf.iot.domain.LegendBean
import com.hyf.iot.domain.device.DeviceItem
import com.hyf.iot.domain.device.ValveUseTime
import com.hyf.iot.presenter.DeviceDetailPresenter
import com.hyf.iot.utils.TimeUtils
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import kotlinx.android.synthetic.main.fakong_datails_layout.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.android.synthetic.main.shebai_list_item.*

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

    override fun getLayoutId(): Int = R.layout.fakong_datails_layout

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
                    Log.e("diff",diff)
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
        if(id.isBlank())
        info1.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    override fun showDetailPage(deviceItem: DeviceItem) {
        if(deviceItem.airSensor != null){
            tv_air_temperature?.text = "${deviceItem.airSensor.airTemperature}°C"
            tv_air_humidity?.text = "${deviceItem.airSensor.airMoisture}%"
        }
        if(!deviceItem.soilSensors.isNullOrEmpty()) {
            val sAdapter = DeviceSoilSensorAdapter(this,deviceItem.soilSensors)
            recycler_view_soil.apply {
                this!!.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL,false)
                adapter = sAdapter
            }
        }else{
            info1.visibility = View.VISIBLE
        }
        if(deviceItem.illuminationSensor != null){
            tv_sun_exposure?.text = "${deviceItem.illuminationSensor.illumination}Lux"
        }
//        tv_device_name?.text = "${valveControlDevices.name}:"
        tv_title.text = deviceItem.name
        tv_device_name.visibility = View.GONE
        tv_device_no.text = deviceItem.number

        tv_state.text = deviceItem.stateString
        when(deviceItem.state){
            0,1 -> {
                tv_state.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gray_oval,0,0,0)
            }
            2 -> {
                tv_state.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green_oval,0,0,0)
            }
            3 -> {
                tv_state.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.red_oval,0,0,0)
            }
        }
        tv_battery_percent.text =  "${(deviceItem.cellVoltageProportion * 100).toInt()}%"
        battery.power = deviceItem.cellVoltageProportion

        val mAdapter = ValveListAdapter(this, deviceItem.valves)
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
        for (i in 0 until deviceItem.valves.size) {
            legendList.add(LegendBean(arrColor[i], deviceItem.valves[i].name))
        }
        legend_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
            adapter = LegendAdapter(this@ValveDetailActivity, legendList)
        }
    }

    override fun initData() {
        getPresenter().getValveUseTimesById(id)
        getPresenter().getDeviceDetailById(id)
    }

}