package com.hyf.iot.fragment.pumb

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import com.hyf.iot.R
import com.hyf.iot.adapter.home.BengChildFragmentAdapter
import com.hyf.iot.common.fragment.BaseFragment
import com.hyf.iot.domain.pumb.WaterPumpValves
import kotlinx.android.synthetic.main.frequency_converter_layout.*

class FrequencyConverterFragment: BaseFragment() {

    private val mAdapter by lazy { BengChildFragmentAdapter(childFragmentManager, mutableListOf()) }

    override fun getLayoutId(): Int  = R.layout.frequency_converter_layout

    override fun initView() {

        viewPager.apply {
            adapter = mAdapter
            currentItem = 0
        }
        tabLayout.apply {
            setupWithViewPager(viewPager)
        }

        val linearLayout = tabLayout.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerDrawable = ContextCompat.getDrawable(mContext!!,
            R.drawable.layout_divider_vertical)
    }

    override fun initData() {
        val waterPumpList = arguments!!.getParcelableArrayList<WaterPumpValves>("data") ?: return
        mAdapter.fragmentList.clear()
        if (waterPumpList.size <= 1)
            tabLayout.visibility = View.GONE
        mAdapter.fragmentList.addAll(waterPumpList)
        mAdapter.notifyDataSetChanged()
    }

}