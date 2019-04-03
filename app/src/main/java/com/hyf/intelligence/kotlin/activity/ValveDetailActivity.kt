package com.hyf.intelligence.kotlin.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.adapter.LegendAdapter
import com.hyf.intelligence.kotlin.adapter.home.ValveListAdapter
import com.hyf.intelligence.kotlin.common.activity.BaseMvpActivity
import com.hyf.intelligence.kotlin.contract.DeviceDetailContract
import com.hyf.intelligence.kotlin.domain.LegendBean
import com.hyf.intelligence.kotlin.domain.device.DeviceItem
import com.hyf.intelligence.kotlin.domain.device.ValveUseTime
import com.hyf.intelligence.kotlin.presenter.DeviceDetailPresenter
import com.hyf.intelligence.kotlin.utils.TimeUtils
import kotlinx.android.synthetic.main.fakong_datails_layout.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.android.synthetic.main.shebai_list_item.*
import java.text.DecimalFormat

class ValveDetailActivity : BaseMvpActivity<DeviceDetailContract.IPresenter>(), DeviceDetailContract.IView {


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

//        val bean1 = FaKongBean("6H", 3f, 9f)
//        val bean2 = FaKongBean("8H", 1f, 8f)
//        val bean3 = FaKongBean("9H12M", 10.3f, 19.5f)
//        val bean4 = FaKongBean("4H", 12f, 16f)
//        val bean5 = FaKongBean("3H", 19f, 22f)
//        val bean6 = FaKongBean("9H", 14f, 23f)
//        val list = ArrayList<FaKongBean>()
//        list.add(bean1);list.add(bean4)
//
//        val list2 = ArrayList<FaKongBean>()
//        list2.add(bean3)
//
//        val list3 = ArrayList<FaKongBean>()
//        list3.add(bean2);list3.add(bean6)
//
//        val list4 = ArrayList<FaKongBean>()
//        list4.add(bean1);list4.add(bean5)
//
//        val lists = ArrayList<ArrayList<FaKongBean>>()
//        lists.add(list);lists.add(list2)
//        lists.add(list3);lists.add(list4)
//
//        val lists2 = ArrayList<ArrayList<FaKongBean>>()
//        lists2.add(list3);lists2.add(list4)
//
//        val mData = ArrayList<ArrayList<ArrayList<FaKongBean>>>()
//        mData.add(lists)
//        mData.add(lists2)
//        mData.add(lists2)
//        horizontalChartView.setDatas(mData)

    }


    override fun showPage(data: MutableList<ValveUseTime>) {
        for (itemDate in data) {
            for (itemUseTime in itemDate.useTime) {

                for (item in itemUseTime.startEnds) {
                    val startH = item.start.substring(11, 13).toFloat()
                    val startM = item.start.substring(14, 16).toFloat()
                    item.start = (startH + (startM.div(60))).toString()

                    val endH = item.end.substring(11, 13).toFloat()
                    val endM = item.end.substring(14, 16).toFloat()
                    item.end = (endH + (endM.div(60))).toString()
                    item.interval = DecimalFormat("0.00").format( item.end.toFloat() - item.start.toFloat()).toString()
                    if(item.interval.endsWith("00")){
                        item.interval = item.interval.substring(0,item.interval.lastIndexOf("00") - 1)
                    }
                    if(item.interval.endsWith("0")){
                        item.interval = item.interval.substring(0,item.interval.lastIndexOf("0"))
                    }
                    Log.e("end", "+++" + item.interval)

                    val diffH = TimeUtils.dateDiff(item.start,item.end,"h")
                    Log.e("diffH", "+++++$diffH")
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