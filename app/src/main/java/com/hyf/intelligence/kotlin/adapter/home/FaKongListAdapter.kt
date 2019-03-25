package com.hyf.intelligence.kotlin.adapter.home

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.activity.FakongDatailsActivity
import com.hyf.intelligence.kotlin.domain.device.Datas
import com.hyf.intelligence.kotlin.utils.newIntent
import java.util.*

class FaKongListAdapter(context: Activity?, id: Int, list: ArrayList<Datas>, getCounts: ValveListAdapter.GetCounts) :
    RecyclerView.Adapter<FaKongListAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId: Int
    private var mData: ArrayList<Datas>
    private var getCounts: ValveListAdapter.GetCounts
    private var adapter : ValveListAdapter? = null
    init {
        this.context = context
        this.mData = list
        layoutId = id
        this.getCounts = getCounts
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        holder.text1?.text = mData[position].str1
        holder.text2?.text = mData[position].str2
        holder.text3?.text = mData[position].str3
        holder.text4?.text = mData[position].str4
        holder.text5?.text = mData[position].str5
        holder.text6?.text = mData[position].str6

        holder.itemView.setOnClickListener {
            context?.newIntent<FakongDatailsActivity>()
        }

        holder.recyclerView.apply {
            this!!.layoutManager = GridLayoutManager(context, 2)
            adapter = ValveListAdapter(this@FaKongListAdapter.context, mData[position].valves,getCounts)
            this@FaKongListAdapter.adapter = adapter as ValveListAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
        ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val text1 = itemview?.findViewById<TextView>(R.id.tv_air_temperature)
        val text2 = itemview?.findViewById<TextView>(R.id.tv_air_humidity)
        val text3 = itemview?.findViewById<TextView>(R.id.tv_soil_temperature)
        val text4 = itemview?.findViewById<TextView>(R.id.tv_soil_humidity)
        val text5 = itemview?.findViewById<TextView>(R.id.tv_sun_exposure)
        val text6 = itemview?.findViewById<TextView>(R.id.tv_device_no)
        val recyclerView = itemview?.findViewById<RecyclerView>(R.id.recycler_view)
    }

    fun getValves() : Int = adapter!!.getValves()

}