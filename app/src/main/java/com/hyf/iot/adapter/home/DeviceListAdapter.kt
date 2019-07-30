package com.hyf.iot.adapter.home

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
import com.hyf.iot.ui.activity.ValveDetailActivity
import com.hyf.iot.domain.device.DeviceItem
import com.hyf.iot.utils.newIntent
import com.hyf.iot.widget.BatteryView
import android.view.MotionEvent
import android.widget.TableRow
import com.hyf.iot.widget.SignalView


class DeviceListAdapter(context: Activity?, var list: MutableList<DeviceItem>) : RecyclerView.Adapter<DeviceListAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId: Int = R.layout.shebai_list_item
    private var mData: MutableList<DeviceItem>
    private lateinit var getCounts: ValveListAdapter.GetCounts
    private var mAdapter : ValveListAdapter? = null
    init {
        this.context = context
        this.mData = list
    }

    fun setGetOunts(getCounts: ValveListAdapter.GetCounts){
        this.getCounts = getCounts
    }

    override fun getItemCount(): Int = mData.size

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        if(item.airSensor != null){
            holder.airTemperature?.text = "${item.airSensor.airTemperature}°C"
            holder.airHumidity?.text = "${item.airSensor.airMoisture}%"
        }
        if(!item.soilSensors.isNullOrEmpty()){
            val sAdapter = DeviceSoilSensorAdapter(context,item.soilSensors)
            holder.recyclerViewSoil.apply {
                this!!.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL,false)
                adapter = sAdapter
            }
        }else{
            holder.info1?.visibility = View.VISIBLE
        }
        if(item.illuminationSensor != null){
            holder.sunExposure?.text = "${item.illuminationSensor.illumination}Lux"
        }
        holder.deviceName?.text = "${item.name}:"
        holder.deviceNo?.text = item.number

        holder.signalView?.setSignalValue(3)
        holder.deviceState?.text = item.stateString
        when(item.state){
            0,1 -> {
                holder.deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.gray_oval,0,0,0)
            }
            2 -> {
                holder.deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.green_oval,0,0,0)
            }
            3 -> {
                holder.deviceState?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.red_oval,0,0,0)
            }
        }
        holder.batteryPercent?.text =  "${(item.cellVoltageProportion * 100).toInt()}%"
        holder.battery?.power = item.cellVoltageProportion

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",item.id)
            context?.newIntent<ValveDetailActivity>(bundle)
        }

        holder.recyclerViewSoil?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                holder.itemView.performClick()  // 模拟父控件的点击
            }
            false
        }

        mAdapter = ValveListAdapter(this@DeviceListAdapter.context, item.valves)
        mAdapter!!.setGetOunts(getCounts)
        holder.recyclerView.apply {
            this!!.layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
        ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val airTemperature = itemview?.findViewById<TextView>(R.id.tv_air_temperature)
        val airHumidity = itemview?.findViewById<TextView>(R.id.tv_air_humidity)
        val deviceState = itemview?.findViewById<TextView>(R.id.tv_state)
        val signalView = itemview?.findViewById<SignalView>(R.id.signalView)
        val batteryPercent = itemview?.findViewById<TextView>(R.id.tv_battery_percent)
        val battery = itemview?.findViewById<BatteryView>(R.id.battery)
        val sunExposure = itemview?.findViewById<TextView>(R.id.tv_sun_exposure)
        val deviceName = itemview?.findViewById<TextView>(R.id.tv_device_name)
        val deviceNo = itemview?.findViewById<TextView>(R.id.tv_device_no)
        val recyclerView = itemview?.findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerViewSoil = itemview?.findViewById<RecyclerView>(R.id.recycler_view_soil)
        val info1 = itemview?.findViewById<TableRow>(R.id.info1)

    }

    fun getValves() : Int = mAdapter!!.getValves()

}