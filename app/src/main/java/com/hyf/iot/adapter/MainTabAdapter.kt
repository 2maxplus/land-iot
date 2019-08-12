package com.hyf.iot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.TabBean
import com.hyf.iot.widget.TabGroupView
import com.hyf.iot.widget.findViewByIdEx

class MainTabAdapter(private val mContext: Context, val mData: List<TabBean>) : TabGroupView.TabAdapter {

    override fun getCount() = mData.size

    override fun getTabView(position: Int, parent: ViewGroup?): View {
        val itemBean = mData[position]
        val view = LayoutInflater.from(mContext).inflate(R.layout.bottom_tab_defalut, parent, false)
        view.findViewByIdEx<ImageView>(R.id.bottom_tab_icon).setImageResource(itemBean.iconResID)
        view.findViewByIdEx<TextView>(R.id.bottom_tab_text).setText(itemBean.textResID)
        return view
    }

}