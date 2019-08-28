package com.hyf.iot.adapter.home

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.devices.DeviceItem
import com.hyf.iot.domain.devices.MoistureStationMassif
import com.hyf.iot.ui.activity.ValveDetailActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.widget.BatteryView
import com.hyf.iot.widget.SignalView

class ValvesExpandableListViewAdapter( context: Activity?, var list: MutableList<MoistureStationMassif>) : BaseExpandableListAdapter() {
    private var context: Activity? = null
    private var valvesData: MutableList<MoistureStationMassif>
    private var mAdapter : ValveListAdapter? = null

    init {
        this.context = context
        this.valvesData = list
    }

    //返回一级列表的个数
    override fun getGroupCount(): Int {
        return valvesData.size
    }

    //返回每个二级列表的个数
    override fun getChildrenCount(groupPosition: Int): Int { //参数groupPosition表示第几个一级列表
        return valvesData[groupPosition].valveControlDevices!!.size
    }

    //返回一级列表的单个item（返回的是对象）
    override fun getGroup(groupPosition: Int): MoistureStationMassif {
        return valvesData[groupPosition]
    }

    //返回二级列表中的单个item（返回的是对象）
    override fun getChild(groupPosition: Int, childPosition: Int): DeviceItem {
        return valvesData[groupPosition].valveControlDevices!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    //每个item的id是否是固定？一般为true
    override fun hasStableIds(): Boolean {
        return true
    }

    //【重要】填充一级列表
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group, null)
        }
        val tv_group = convertView!!.findViewById<TextView>(R.id.tv_group)
        tv_group.text = getGroup(groupPosition).massifName
        return convertView
    }

    //【重要】填充二级列表
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun getChildView(
            groupPosition: Int,
            childPosition: Int,
            isLastChild: Boolean,
            convertView: View?,
            parent: ViewGroup
    ): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shebai_list_item, null)
        }

        val airTemperature = convertView!!.findViewById<TextView>(R.id.tv_air_temperature)
        val airHumidity = convertView.findViewById<TextView>(R.id.tv_air_humidity)
        val deviceState = convertView.findViewById<TextView>(R.id.tv_state)
        val signalView = convertView.findViewById<SignalView>(R.id.signalView)
        val batteryPercent = convertView.findViewById<TextView>(R.id.tv_battery_percent)
        val battery = convertView.findViewById<BatteryView>(R.id.battery)
        val sunExposure = convertView.findViewById<TextView>(R.id.tv_sun_exposure)
        val deviceName = convertView.findViewById<TextView>(R.id.tv_device_name)
        val deviceNo = convertView.findViewById<TextView>(R.id.tv_device_no)
        val recyclerView = convertView.findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerViewSoil = convertView.findViewById<RecyclerView>(R.id.recycler_view_sensor)
        val info1 = convertView.findViewById<TableRow>(R.id.info1)

        val item = getChild(groupPosition,childPosition)
        if(item.airSensor != null){
            airTemperature?.text = "${item.airSensor.airTemperature}°C"
            airHumidity?.text = "${item.airSensor.airMoisture}%"
        }
        if(!item.soilSensors.isNullOrEmpty()){
            val sAdapter = DeviceSoilSensorAdapter(context,item.soilSensors)
            recyclerViewSoil.apply {
                this!!.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL,false)
                adapter = sAdapter
            }
        }else{
            info1?.visibility = View.VISIBLE
        }
        if(item.illuminationSensor != null){
            sunExposure?.text = "${item.illuminationSensor.illumination}Lux"
        }
        deviceName?.text = "${item.name}:"
        deviceNo?.text = item.number

        signalView?.setSignalValue(item.signalIntensityProportion)

        deviceState?.text = item.stateString
        when(item.state){
            0,1 -> {
                deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gray_oval,0,0,0)
            }
            2 -> {
                deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green_oval,0,0,0)
            }
            3 -> {
                deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.red_oval,0,0,0)
            }
        }
        batteryPercent?.text =  "${(item.cellVoltageProportion * 100).toInt()}%"
        battery?.power = item.cellVoltageProportion

        convertView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",item.id)
            context?.newIntent<ValveDetailActivity>(bundle)
        }
        recyclerViewSoil?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                convertView.performClick()  // 模拟父控件的点击
            }
            false
        }
        mAdapter = ValveListAdapter(this@ValvesExpandableListViewAdapter.context, item.valves!!)
        mAdapter!!.setGetOunts(object : ValveListAdapter.GetCounts {
            override fun adds() {
            }
            override fun subs() {
            }
        })
        recyclerView.apply {
            this!!.layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }

        return convertView
    }

    //二级列表中的item是否能够被选中？可以改为true
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}