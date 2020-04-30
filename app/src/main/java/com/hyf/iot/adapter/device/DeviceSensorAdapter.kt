package com.hyf.iot.adapter.device

import android.annotation.SuppressLint
import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.device.SensorOtherInfo
import java.util.*

class DeviceSensorAdapter(context: Activity?, list : ArrayList<SensorOtherInfo>) : androidx.recyclerview.widget.RecyclerView.Adapter<DeviceSensorAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId:Int = R.layout.item_device_sensor
    private var mData: ArrayList<SensorOtherInfo>

    init {
        this.context = context
        this.mData = list
    }

    override fun getItemCount(): Int = mData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        holder.title?.text = item.name // "${item.soilTemperature}°C"
        holder.value?.text = "${item.value} ${item.unit}"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
            ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))

    class ViewHolders(itemview: View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemview!!) {
        val title = itemview?.findViewById<TextView>(R.id.tv_title)
        val value = itemview?.findViewById<TextView>(R.id.tv_value)
    }

}