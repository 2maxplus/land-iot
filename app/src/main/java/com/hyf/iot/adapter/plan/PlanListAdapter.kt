package com.hyf.iot.adapter.plan

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.plan.IrrigatePlanGroupControlsInfo
import com.hyf.iot.widget.SignalView

class PlanListAdapter(context: Context?, var list : MutableList<IrrigatePlanGroupControlsInfo>) : RecyclerView.Adapter<PlanListAdapter.ViewHolders>() {
    private var context: Context? = null
    private var layoutId:Int = R.layout.item_plan_device_list
    private var mData: MutableList<IrrigatePlanGroupControlsInfo>

    init {
        this.context = context
        this.mData = list
    }

    override fun getItemCount(): Int = mData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        holder.content?.text = item.deviceName + "   " + item.sensorName  // + item.deviceStateString  + item.sensorStateString
        holder.signalView?.setSignalValue(item.deviceSignal)
        holder.switchState?.isChecked = when(item.sensorState){
            4 -> true
            2 -> false
            else -> false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
            ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val content = itemview?.findViewById<TextView>(R.id.content)
        val signalView = itemview?.findViewById<SignalView>(R.id.signalView)
        val switchState = itemview?.findViewById<Switch>(R.id.switch_state)
    }

}