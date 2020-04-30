package com.hyf.iot.adapter.device

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.device.DeviceInfo
import com.hyf.iot.ui.activity.ValveDetailActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.widget.BatteryView
import com.hyf.iot.widget.SignalView


class DeviceListAdapter(context: Activity?, var list: MutableList<DeviceInfo>) : RecyclerView.Adapter<DeviceListAdapter.ViewHolders>() {

    private var context: Activity? = null
    private var layoutId: Int = R.layout.item_device
    private var mData: MutableList<DeviceInfo>
    private lateinit var getCounts: ValveListAdapter.GetCounts
    private var mAdapter: ValveListAdapter? = null

    init {
        this.context = context
        this.mData = list
    }

    fun setGetOunts(getCounts: ValveListAdapter.GetCounts) {
        this.getCounts = getCounts
    }

    override fun getItemCount(): Int = mData.size

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]

        if (!item.sensor_OtherInfos.isNullOrEmpty()) {
            val sAdapter = DeviceSensorAdapter(context, item.sensor_OtherInfos)
            holder.recyclerViewSensor.apply {
                this!!.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
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
            holder.batteryPercent?.text = "${(item.sensor_VoltageInfo.value ).toInt()}%"
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
            mAdapter = ValveListAdapter(this@DeviceListAdapter.context, item.sensor_ValveInfos)
            mAdapter!!.setGetOunts(getCounts)
            holder.recyclerView.apply {
                this!!.layoutManager = GridLayoutManager(context, 2)
                adapter = mAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
            ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))


    class ViewHolders(itemview: View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemview!!) {
        val deviceState = itemview?.findViewById<TextView>(R.id.tv_state)
        val signalView = itemview?.findViewById<SignalView>(R.id.signalView)
        val batteryPercent = itemview?.findViewById<TextView>(R.id.tv_battery_percent)
        val battery = itemview?.findViewById<BatteryView>(R.id.battery)
        val deviceName = itemview?.findViewById<TextView>(R.id.tv_device_name)
        val deviceNo = itemview?.findViewById<TextView>(R.id.tv_device_no)
        val recyclerView = itemview?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)
        val recyclerViewSensor = itemview?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view_sensor)

    }

    fun getValves(): Int = mAdapter!!.getValves()

}