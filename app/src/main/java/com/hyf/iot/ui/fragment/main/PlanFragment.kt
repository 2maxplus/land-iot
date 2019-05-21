package com.hyf.iot.ui.fragment.main

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import com.hyf.iot.R
import com.hyf.iot.adapter.home.PlanFragmentAdapter
import com.hyf.iot.common.fragment.BaseFragment
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.plan_layout.*

class PlanFragment: BaseFragment() {

    private lateinit var titleList: ArrayList<String>
    private lateinit var planFragmentAdapter: PlanFragmentAdapter

    override fun getLayoutId(): Int = R.layout.plan_layout

    override fun initView() {
        iv_back.visibility = View.GONE
        tv_title.text = "灌溉计划"
        titleList = ArrayList()
        titleList.add("在执行"); titleList.add("已执行"); titleList.add("已暂停"); titleList.add("未执行")

        planFragmentAdapter = PlanFragmentAdapter(childFragmentManager, titleList)

        viewPager.adapter = planFragmentAdapter
        viewPager.currentItem = 0

        tabLayout.setupWithViewPager(viewPager)
//        tabLayout.tabRippleColor = ColorStateList.valueOf(resources.getColor(R.color.transparent))

        for (i in 0..titleList.size){
            tabLayout.getTabAt(i)?.text = titleList[i]
        }
        val linearLayout = tabLayout.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerDrawable = ContextCompat.getDrawable(mContext!!,
            R.drawable.layout_divider_vertical)
    }

    override fun initData() {
    }

}