package com.hyf.iot.adapter.device

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.hyf.iot.R
import com.hyf.iot.domain.device.DeviceInfo
import com.hyf.iot.domain.device.MoistureStationMassif
import com.hyf.iot.ui.activity.ValveDetailActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.widget.BatteryView
import com.hyf.iot.widget.SignalView
import com.hyf.iot.widget.dialog.CountDownDialog

class ValvesExpandableListViewAdapter(context: Activity?, var valvesData: MutableList<MoistureStationMassif>) : BaseExpandableListAdapter() {
    private var context: Activity? = null
//    private var valvesData: MutableList<MoistureStationMassif>
    private var mAdapter: ValveListAdapter? = null

    init {
        this.context = context
//        this.valvesData = list
    }
    private var mDialog: CountDownDialog? = null
    fun setCountDownDialog(dialog: CountDownDialog){
        this.mDialog = dialog
    }

    //返回一级列表的个数
    override fun getGroupCount(): Int {
        return valvesData.size
    }

    //返回每个二级列表的个数
    override fun getChildrenCount(groupPosition: Int): Int { //参数groupPosition表示第几个一级列表
        if(groupPosition < valvesData.size) {
            return valvesData[groupPosition].valveControlDevices!!.size
        }else {
            return valvesData[0].valveControlDevices!!.size
        }
    }

    //返回一级列表的单个item（返回的是对象）
    override fun getGroup(groupPosition: Int): MoistureStationMassif {
        return if(groupPosition < valvesData.size){
            valvesData[groupPosition]
        } else {
            valvesData[0]
        }
    }

    //返回二级列表中的单个item（返回的是对象）
    override fun getChild(groupPosition: Int, childPosition: Int): DeviceInfo {
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
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n", "WrongConstant")
    override fun getChildView(
            groupPosition: Int,
            childPosition: Int,
            isLastChild: Boolean,
            convertView: View?,
            parent: ViewGroup
    ): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_device, null)
        }

        val deviceState = convertView!!.findViewById<TextView>(R.id.tv_state)
        val signalView = convertView.findViewById<SignalView>(R.id.signalView)
        val batteryPercent = convertView.findViewById<TextView>(R.id.tv_battery_percent)
        val battery = convertView.findViewById<BatteryView>(R.id.battery)
        val deviceName = convertView.findViewById<TextView>(R.id.tv_device_name)
        val deviceNo = convertView.findViewById<TextView>(R.id.tv_device_no)
        val recyclerView = convertView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
        val recyclerViewSoil = convertView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view_sensor)

        val item = getChild(groupPosition, childPosition)

//        if (!item.sensor_OtherInfos.isNullOrEmpty()) {
            val sAdapter = DeviceSensorAdapter(context, item.sensor_OtherInfos!!)
            recyclerViewSoil.apply {
                this!!.layoutManager = LinearLayoutManager(context, VERTICAL, false)
                adapter = sAdapter
            }
//        }

        deviceName?.text = "${item.name}:"
        deviceNo?.text = item.number
        if (item.sensor_SignalInfo != null)
            signalView?.setSignalValue(item.sensor_SignalInfo.value)

        deviceState?.text = item.stateString
        when (item.state) {
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
        if (item.sensor_VoltageInfo != null) {
            batteryPercent?.text = "${(item.sensor_VoltageInfo.value).toInt()}%"
            battery?.power = item.sensor_VoltageInfo.value / 100
        }
        convertView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", item.id)
            context?.newIntent<ValveDetailActivity>(bundle)
        }
        recyclerViewSoil.setOnTouchListener { _, event ->
            convertView.onTouchEvent(event)
        }
        if (item.sensor_ValveInfos != null) {
            mAdapter = ValveListAdapter(this@ValvesExpandableListViewAdapter.context, item.sensor_ValveInfos)
            mAdapter!!.setCountDownDialog(mDialog!!)
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
        }
        return convertView
    }

    //二级列表中的item是否能够被选中？可以改为true
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}