package com.hyf.iot.adapter.device

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.pumb.PumpStation
import com.hyf.iot.domain.pumb.SensorInfo
import com.hyf.iot.widget.BatteryView
import com.hyf.iot.widget.SignalView
import com.hyf.iot.widget.dialog.CountDownDialog

class PumpStationExpandableListViewAdapter(context: Activity?, var list: MutableList<PumpStation>) : BaseExpandableListAdapter() {
    private var context: Activity? = null

    init {
        this.context = context
    }
    private var mDialog: CountDownDialog? = null
    fun setCountDownDialog(dialog: CountDownDialog){
        this.mDialog = dialog
    }

    //返回一级列表的个数
    override fun getGroupCount(): Int {
        return list.size
    }

    //返回每个二级列表的个数
    override fun getChildrenCount(groupPosition: Int): Int { //参数groupPosition表示第几个一级列表
        return if(groupPosition < list.size) {
            list[groupPosition].sensorInfo.size
        }else {
            list[0].sensorInfo.size
        }
    }

    //返回一级列表的单个item（返回的是对象）
    override fun getGroup(groupPosition: Int): PumpStation {
        return if(groupPosition < list.size){
            list[groupPosition]
        }else{
            list[0]
        }
    }

    //返回二级列表中的单个item（返回的是对象）
    override fun getChild(groupPosition: Int, childPosition: Int): SensorInfo {
        return list[groupPosition].sensorInfo[childPosition]
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
    @SuppressLint("SetTextI18n")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pumpstation_group, null)
        }
        val deviceState = convertView!!.findViewById<TextView>(R.id.tv_state)
        val signalView = convertView.findViewById<SignalView>(R.id.signalView)
        val batteryPercent = convertView.findViewById<TextView>(R.id.tv_battery_percent)
        val battery = convertView.findViewById<BatteryView>(R.id.battery)
        val deviceName = convertView.findViewById<TextView>(R.id.tv_device_name)
        val deviceNo = convertView.findViewById<TextView>(R.id.tv_device_no)
        val temperature = convertView.findViewById<TextView>(R.id.tv_temperature)

        val item = getGroup(groupPosition)

        deviceName?.text = item.name
        deviceNo?.text = item.number

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

        signalView?.setSignalValue(item.signalIntensity)

        batteryPercent?.text = "${item.cellVoltage}%"
        battery?.power = item.cellVoltage.toFloat() / 100

        temperature?.text = "温度:${item.temperature}℃"

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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pumpstation_child, null)
        }
        val title = convertView!!.findViewById<TextView>(R.id.tv_title)
        val number = convertView.findViewById<TextView>(R.id.tv_no)
        val value = convertView.findViewById<TextView>(R.id.tv_value)
        val updateTime = convertView.findViewById<TextView>(R.id.tv_update_time)

        val item = getChild(groupPosition, childPosition)

        title?.text = "${item.name}"
        number?.text = item.serialNumber.toString()
        value?.text = item.value.toString()
        updateTime?.text = item.updated.substring(5,item.updated.length)
        return convertView
    }

    //二级列表中的item是否能够被选中？可以改为true
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }
}