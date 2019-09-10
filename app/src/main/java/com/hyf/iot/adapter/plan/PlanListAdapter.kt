package com.hyf.iot.adapter.plan

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.plan.IrrigatePlanGroupControlsInfo
import java.util.*

class PlanListAdapter(context: Activity?, list : ArrayList<IrrigatePlanGroupControlsInfo>) : RecyclerView.Adapter<PlanListAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId:Int = R.layout.plan_list_item_layout
    private var mData: ArrayList<IrrigatePlanGroupControlsInfo>

    init {
        this.context = context
        this.mData = list
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        if (position == mData.size-1){
            holder.line2?.visibility = View.INVISIBLE
        }

//        holder.time?.text = item.time
        holder.content?.text = item.deviceName +"   "+ item.sensorName

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


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val time = itemview?.findViewById<TextView>(R.id.time)
        val line1 = itemview?.findViewById<View>(R.id.line1)
        val line2 = itemview?.findViewById<View>(R.id.line2)
        val content = itemview?.findViewById<TextView>(R.id.content)
        val switchs = itemview?.findViewById<TextView>(R.id.switchs)
    }

}