package com.hyf.intelligence.iot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.domain.TabBean
import com.hyf.intelligence.iot.widget.TabGroupView
import com.hyf.intelligence.iot.widget.findViewByIdEx

/**
 * Created by L on 2017/7/12.
 */
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