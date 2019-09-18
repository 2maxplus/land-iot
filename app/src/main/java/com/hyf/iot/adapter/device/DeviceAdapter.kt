package com.hyf.iot.adapter.device

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.device.DeviceInfo
import com.hyf.iot.domain.farm.Massif
import com.hyf.iot.ui.activity.ValveDetailActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.widget.BatteryView
import com.hyf.iot.widget.MyLinearLayoutManager
import com.hyf.iot.widget.SignalView
import com.hyf.iot.widget.findViewByIdEx
import com.hyf.iot.widget.loadmore.LoadMoreRecyclerAdapter


class DeviceAdapter(context: Activity, mData: MutableList<DeviceInfo>) : LoadMoreRecyclerAdapter<DeviceInfo>(context, mData) {

    private var context: Activity? = null
    private lateinit var getCounts: ValveListAdapter.GetCounts
    private var mAdapter: ValveListAdapter? = null

    init {
        this.context = context
    }

    fun setGetOunts(getCounts: ValveListAdapter.GetCounts) {
        this.getCounts = getCounts
    }

    override fun getItemHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            ViewHolders(LayoutInflater.from(mContext).inflate(R.layout.item_device, parent, false))

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolders){
            val item = mData[position]
            if (!item.sensor_OtherInfos.isNullOrEmpty()) {
                val sAdapter = DeviceSensorAdapter(context, item.sensor_OtherInfos)
                val linearLayoutManager =  MyLinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
//                linearLayoutManager.setScrollEnabled(false)
                holder.recyclerViewSensor.apply {
                    this!!.layoutManager = linearLayoutManager
                    adapter = sAdapter
                }
            }

            holder.deviceName?.text = "${item.name}:"
            holder.deviceNo?.text = item.number
            if (item.sensor_SignalInfo != null)
                holder.signalView?.setSignalValue(item.sensor_SignalInfo.value)
            holder.deviceState?.text = item.stateString
            when (item.state) {
                0, 1 -> {
                    holder.deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gray_oval, 0, 0, 0)
                }
                2 -> {
                    holder.deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green_oval, 0, 0, 0)
                }
                3 -> {
                    holder.deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.red_oval, 0, 0, 0)
                }
            }
            if (item.sensor_VoltageInfo != null) {
                holder.batteryPercent?.text = "${(item.sensor_VoltageInfo.value).toInt()}%"
                holder.battery?.power = item.sensor_VoltageInfo.value / 100
            }

            holder.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", item.id)
                context?.newIntent<ValveDetailActivity>(bundle)
            }

            holder.recyclerViewSensor?.setOnTouchListener { _, event ->
                holder.itemView.onTouchEvent(event)
            }

            if (item.sensor_ValveInfos != null) {
                mAdapter = ValveListAdapter(this@DeviceAdapter.context, item.sensor_ValveInfos)
                mAdapter!!.setGetOunts(getCounts)
                holder.recyclerView.apply {
                    this!!.layoutManager = GridLayoutManager(context, 2)
                    adapter = mAdapter
                }
            }
        }
    }

    class ViewHolders(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val deviceState by lazy { itemview?.findViewByIdEx<TextView>(R.id.tv_state) }
        val signalView by lazy { itemview?.findViewById<SignalView>(R.id.signalView) }
        val batteryPercent by lazy { itemview?.findViewById<TextView>(R.id.tv_battery_percent) }
        val battery by lazy { itemview?.findViewById<BatteryView>(R.id.battery) }
        val deviceName by lazy { itemview?.findViewById<TextView>(R.id.tv_device_name) }
        val deviceNo by lazy { itemview?.findViewById<TextView>(R.id.tv_device_no) }
        val recyclerView by lazy { itemview?.findViewById<RecyclerView>(R.id.recycler_view) }
        val recyclerViewSensor by lazy { itemview?.findViewById<RecyclerView>(R.id.recycler_view_sensor) }

    }

    fun getValves(): Int = mAdapter!!.getValves()

}