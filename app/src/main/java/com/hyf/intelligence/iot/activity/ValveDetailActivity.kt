package com.hyf.intelligence.iot.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.hyf.intelligence.iot.App
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.adapter.LegendAdapter
import com.hyf.intelligence.iot.adapter.home.ValveListAdapter
import com.hyf.intelligence.iot.common.activity.BaseMvpActivity
import com.hyf.intelligence.iot.contract.DeviceDetailContract
import com.hyf.intelligence.iot.domain.LegendBean
import com.hyf.intelligence.iot.domain.device.DeviceItem
import com.hyf.intelligence.iot.domain.device.ValveUseTime
import com.hyf.intelligence.iot.presenter.DeviceDetailPresenter
import com.hyf.intelligence.iot.utils.TimeUtils
import com.hyf.intelligence.iot.utils.newIntent
import com.hyf.intelligence.iot.utils.showToast
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
        tv_title.text = "阀控详情"
    }


    override fun showPage(data: MutableList<ValveUseTime>) {
        for (itemDate in data) {
            for (itemUseTime in itemDate.useTime) {

                for (item in itemUseTime.startEnds) {
                    val diff = TimeUtils.dateDiff(item.start, item.end)
                    item.interval = diff.toString()

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

    override fun errorPage(t: Throwable) {

    }

    @SuppressLint("SetTextI18n")
    override fun showDetailPage(deviceItem: DeviceItem) {
        tv_air_temperature?.text = "${deviceItem.airSensor.airTemperature}°C"
        tv_air_humidity?.text = "${deviceItem.airSensor.airMoisture}%"
        tv_soil_temperature?.text = "${deviceItem.soilSensors[0].soilTemperature}°C"
        tv_soil_humidity?.text = "${deviceItem.soilSensors[0].soilMoisture}%"
        tv_sun_exposure?.text = "${deviceItem.illuminationSensor.illumination}Lux"
        tv_device_name?.text = "${deviceItem.name}:"
        tv_device_no?.text = deviceItem.number

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