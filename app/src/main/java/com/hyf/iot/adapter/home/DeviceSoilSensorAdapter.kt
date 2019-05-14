package com.hyf.iot.adapter.home

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.device.SoilSensor
import java.util.*

class DeviceSoilSensorAdapter(context: Activity?, list : ArrayList<SoilSensor>) : RecyclerView.Adapter<DeviceSoilSensorAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId:Int = R.layout.soil_sensor_item
    private var mData: ArrayList<SoilSensor>

    init {
        this.context = context
        this.mData = list
    }

    override fun getItemCount(): Int = mData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        holder.teperature?.text = "${item.soilTemperature}°C"
        holder.moisture?.text = "${item.soilMoisture}%"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
        ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val teperature = itemview?.findViewById<TextView>(R.id.tv_soil_temperature)
        val moisture = itemview?.findViewById<TextView>(R.id.tv_soil_moisture)
    }

}