package com.hyf.intelligence.kotlin.adapter.home

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.activity.ValveDetailActivity
import com.hyf.intelligence.kotlin.domain.device.DeviceItem
import com.hyf.intelligence.kotlin.utils.newIntent

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        holder.airTemperature?.text = "${item.airSensor.airTemperature}°C"
        holder.airHumidity?.text = "${item.airSensor.airMoisture}%"
        holder.soilTemperature?.text = "${item.soilSensors[0].soilTemperature}°C"
        holder.soilHumidity?.text = "${item.soilSensors[0].soilMoisture}%"
        holder.sunExposure?.text = "${item.illuminationSensor.illumination}Lux"
        holder.deviceName?.text = "${item.name}:"
        holder.deviceNo?.text = item.number

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id",item.id)
            context?.newIntent<ValveDetailActivity>(bundle)
        }

        mAdapter = ValveListAdapter(this@DeviceListAdapter.context, mData[position].valves)
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
        val soilTemperature = itemview?.findViewById<TextView>(R.id.tv_soil_temperature)
        val soilHumidity = itemview?.findViewById<TextView>(R.id.tv_soil_humidity)
        val sunExposure = itemview?.findViewById<TextView>(R.id.tv_sun_exposure)
        val deviceName = itemview?.findViewById<TextView>(R.id.tv_device_name)
        val deviceNo = itemview?.findViewById<TextView>(R.id.tv_device_no)
        val recyclerView = itemview?.findViewById<RecyclerView>(R.id.recycler_view)
    }

    fun getValves() : Int = mAdapter!!.getValves()

}