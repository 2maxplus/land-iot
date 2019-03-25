package com.hyf.intelligence.kotlin.fragment.pumb

import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.adapter.home.BengChildFragmentAdapter
import com.hyf.intelligence.kotlin.common.fragment.BaseFragment
import com.hyf.intelligence.kotlin.domain.pumb.WaterPump
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
        val waterPumpList = arguments!!.getParcelableArrayList<WaterPump>("data") ?: return
        mAdapter.fragmentList.clear()
        mAdapter.fragmentList.addAll(waterPumpList)
        mAdapter.notifyDataSetChanged()
    }

}