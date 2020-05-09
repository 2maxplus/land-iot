package com.hyf.iot.adapter.plan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.plan.IrrigatePlanGroupControlsInfo
import java.util.*

class PlanListAdapter(context: Context?, var list : MutableList<IrrigatePlanGroupControlsInfo>) : RecyclerView.Adapter<PlanListAdapter.ViewHolders>() {
    private var context: Context? = null
    private var layoutId:Int = R.layout.item_plan_list
    private var mData: MutableList<IrrigatePlanGroupControlsInfo>

    init {
        this.context = context
        this.mData = list
    }

    override fun getItemCount(): Int = mData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        val time = item.updated.replace(" ","\n")
        holder.time?.text = time
        holder.content?.text = item.deviceName + item.deviceStateString+"   "+ item.sensorName + item.sensorStateString

//        when(mData[position].type){
//            1-> {
//                holder.content?.setTextColor(Color.GRAY)
//                holder.switchs?.setTextColor(Color.GRAY)
//            }
//            2-> {
//                holder.content?.setTextColor(context!!.resources.getColor(R.color.text_green))
//                holder.switchs?.setTextColor(context!!.resources.getColor(R.color.text_green))
//            }
//            3-> {
//                holder.content?.setTextColor(context!!.resources.getColor(R.color.text_blue))
//                holder.switchs?.setTextColor(context!!.resources.getColor(R.color.text_blue))
//            }
//            4->{
//                holder.content?.setTextColor(context!!.resources.getColor(R.color.colorAccent))
//                holder.switchs?.setTextColor(context!!.resources.getColor(R.color.colorAccent))
//            }
//        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
            ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))


    class ViewHolders(itemview: View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemview!!) {
        val time = itemview?.findViewById<TextView>(R.id.time)
        val content = itemview?.findViewById<TextView>(R.id.content)
    }

}