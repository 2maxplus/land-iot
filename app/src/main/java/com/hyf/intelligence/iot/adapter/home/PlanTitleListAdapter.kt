package com.hyf.intelligence.iot.adapter.home

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.hyf.intelligence.iot.R
import java.util.*

class PlanTitleListAdapter(context: Activity?, id:Int, list : ArrayList<String>) : RecyclerView.Adapter<PlanTitleListAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId:Int
    private var mData: ArrayList<String>

    init {
        this.context = context
        this.mData = list
        layoutId = id
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        holder.title?.text = mData[position]

        holder.title?.setOnClickListener {
            for (i in 0..mData.size){
                holder.title.isChecked = i == position
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
        ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val title = itemview?.findViewById<CheckBox>(R.id.title)
    }

}