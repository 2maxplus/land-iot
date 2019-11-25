package com.hyf.iot.ui.fragment.plan

import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.hyf.iot.R
import com.hyf.iot.adapter.plan.PlanFragmentAdapter
import com.hyf.iot.common.fragment.BaseFragment
import com.hyf.iot.domain.plan.StateType
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.plan_layout.*

class PlanFragment : BaseFragment() {

    private var titleList: ArrayList<StateType> = ArrayList()
    private lateinit var planFragmentAdapter: PlanFragmentAdapter

    override fun getLayoutId(): Int = R.layout.plan_layout

    init {
        titleList.add(StateType("", "全部"))
        titleList.add(StateType("1", "在执行"))
        titleList.add(StateType("3", "已执行"))
        titleList.add(StateType("2", "已暂停"))
        titleList.add(StateType("0", "未执行"))
    }

    override fun initView() {
        iv_back.visibility = View.GONE
        tv_title.text = "灌溉计划"

        planFragmentAdapter = PlanFragmentAdapter(childFragmentManager, titleList)
        viewPager.apply {
            offscreenPageLimit = titleList.size
            adapter = planFragmentAdapter
            currentItem = 0
            addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                }

                override fun onPageSelected(p0: Int) {
                    ((childFragmentManager.fragments[viewPager.currentItem]) as PlanChildFragment).onReload()
                    ((childFragmentManager.fragments[viewPager.currentItem]) as PlanChildFragment).setOperateBtnByState(titleList[p0].state)
                }
            })
        }
        Log.i("---","childFragmentManager.fragments:"+planFragmentAdapter.titleList.size)
        tabLayout.setupWithViewPager(viewPager)
//        tabLayout.tabRippleColor = ColorStateList.valueOf(resources.getColor(R.color.transparent))

        for (i in 0..titleList.size) {
            tabLayout.getTabAt(i)?.text = titleList[i].title
        }
        val linearLayout = tabLayout.getChildAt(0) as LinearLayout
        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerDrawable = ContextCompat.getDrawable(context!!,
                R.drawable.layout_divider_vertical)

        tv_operate.visibility = View.VISIBLE
        tv_operate.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_refresh, 0, 0, 0)
        tv_operate.setOnClickListener {
            if (childFragmentManager.fragments.size > 0)
                ((childFragmentManager.fragments[viewPager.currentItem]) as PlanChildFragment).onReload()
        }
    }

}