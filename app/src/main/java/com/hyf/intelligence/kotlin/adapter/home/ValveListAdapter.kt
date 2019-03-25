package com.hyf.intelligence.kotlin.adapter.home

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.domain.device.Valves

class ValveListAdapter(context: Activity?, list: ArrayList<Valves>, getCounts: GetCounts?) :
    RecyclerView.Adapter<ValveListAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var mData: ArrayList<Valves>
    private var getCounts: GetCounts
    private var valvesCount :Int = 0
    init {
        this.context = context
        this.mData = list
        this.getCounts = getCounts!!
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val valve = mData[position]
        holder.tv_valve_name?.text = valve.valveName

        when (valve.state) {
            1 ->
            {
                holder.switch_state?.isChecked = true
                valvesCount ++
            }
            0 -> holder.switch_state?.isChecked = false
        }

        holder.switch_state?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
//                valvesCount ++
                getCounts.adds()
            } else {
//                valvesCount --
                getCounts.subs()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
        ViewHolders(LayoutInflater.from(context).inflate(R.layout.valve_operate_item, parent, false))


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val tv_valve_name = itemview?.findViewById<TextView>(R.id.tv_valve_name)
        val switch_state = itemview?.findViewById<Switch>(R.id.switch_state)
    }

    interface GetCounts {
        fun adds()
        fun subs()
    }

    fun getValves() : Int = valvesCount

}