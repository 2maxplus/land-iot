package com.hyf.intelligence.kotlin.adapter.home

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.domain.device.FaKongBean
import com.hyf.intelligence.kotlin.widget.chart.HorizontalChartView
import java.util.*

class BengListAdapter(context: Activity?, id:Int, bengList: ArrayList<ArrayList<FaKongBean>>) : RecyclerView.Adapter<BengListAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId:Int
    private var bengList: ArrayList<ArrayList<FaKongBean>>

    init {
        this.context = context
        layoutId = id
        this.bengList = bengList
    }

    override fun getItemCount(): Int = bengList.size

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
//        holder.horizontalChartView?.setData(bengList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
        ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val horizontalChartView = itemview?.findViewById<HorizontalChartView>(R.id.horizontalChartView)
    }

}