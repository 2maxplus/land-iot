package com.hyf.iot.widget.spinner

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.iot.R
import java.util.ArrayList

class SpinnerChooseAdapter(private val context: Context, private val itemClickListener: MyItemClickListener, private val list: ArrayList<String>?, private val selectPosition: Int) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private val id: String? = null
    private val name: String? = null
    private val patientId: String? = null
    private val cardNo: String? = null

    interface MyItemClickListener {
        fun onClick(view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popupwindow_spinner_item, null)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        view.setOnClickListener { view -> itemClickListener.onClick(view) }
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.itemView.tag = position
            holder.textView.text = list!![position]
            holder.textView.isSelected = position == selectPosition
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var textView: TextView = itemView.findViewById(R.id.choose_item)

    }
}