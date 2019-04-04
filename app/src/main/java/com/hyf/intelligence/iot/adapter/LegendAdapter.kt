package com.hyf.intelligence.iot.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.domain.LegendBean

@Suppress("CAST_NEVER_SUCCEEDS")
class LegendAdapter(context: Activity?, var list: MutableList<LegendBean>) : RecyclerView.Adapter<LegendAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId: Int = R.layout.legend_layout
    private var mData: MutableList<LegendBean>
    init {
        this.context = context
        this.mData = list
    }


    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        holder.legendColor?.setBackgroundResource(item.iconResID)
        holder.legendText?.text = item.textResID


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
            ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val legendColor = itemview?.findViewById<View>(R.id.legend_color)
        val legendText = itemview?.findViewById<TextView>(R.id.legend_text)
    }


}