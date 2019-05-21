package com.hyf.iot.ui.fragment.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import com.hyf.iot.R
import com.hyf.iot.adapter.home.PlanListAdapter
import com.hyf.iot.common.fragment.BaseFragment
import com.hyf.iot.domain.device.PlanBean
import kotlinx.android.synthetic.main.layout_refresh_recycler_view.*
import kotlinx.android.synthetic.main.layout_verticaltab_recycleview.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView


@SuppressLint("ValidFragment")
class PlanChildFragment(position : Int): BaseFragment() {

    private lateinit var list: ArrayList<PlanBean>

    private lateinit var titleList: ArrayList<String>
    private val curPos = position

    override fun getLayoutId(): Int = R.layout.layout_verticaltab_recycleview

    override fun initView() {

        titleList = ArrayList()
        titleList.add("计划1"); titleList.add("计划2"); titleList.add("计划3"); titleList.add("计划4"); titleList.add("计划5"); titleList.add("计划6"); titleList.add("计划7")

        var type = 1
        when(curPos){
            0 -> type = 2
            1 -> type = 1
            2 -> type = 4
            3 -> type = 3
        }

        list = ArrayList()

        val  planBean = PlanBean("1-10\n07:21","泵0xffuy162 开   阀门3   开",type)
        list.add(planBean)

        val  planBean1 = PlanBean("2-11\n09:21","泵0xffuy162 开   阀门3   开",type)
        list.add(planBean1)

        val  planBean2 = PlanBean("3-10\n11:21","泵0xffuy162 开   阀门3   开",type)
        list.add(planBean2)

        val  planBean3 = PlanBean("5-21\n02:21","泵0xffuy122 开   阀门4   开",type)
        list.add(planBean3)

        val  planBean4 = PlanBean("6-13\n14:21","泵0xffuy112 关   阀门5   关",type)
        list.add(planBean4)

        val  planBean5 = PlanBean("8-10\n18:21","泵0xffuy192 关   阀门6   关",type)
        list.add(planBean5)

        for(i in 0..30){
            val  planBean = PlanBean("1-10\n07:21","泵0xffuy162 开   阀门3   开",type)
            list.add(planBean)
        }

        tablayout.setTabAdapter(object : TabAdapter{
            override fun getIcon(position: Int): ITabView.TabIcon? {
                return null
            }

            override fun getBadge(position: Int): ITabView.TabBadge? {
                return null
            }

            override fun getBackground(position: Int): Int {
                return 0
            }

            override fun getTitle(position: Int): ITabView.TabTitle {
                return ITabView.TabTitle.Builder()
                    .setContent(titleList[position])
                    .setTextColor(activity!!.resources.getColor(R.color.text_blue),Color.GRAY)
                    .build()
            }

            override fun getCount(): Int {
               return titleList.size
            }

        })

        tablayout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabView, position: Int) {

            }

            override fun onTabReselected(tab: TabView, position: Int) {

            }
        })

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = PlanListAdapter(activity,R.layout.plan_list_item_layout,list)
        }

        refresh_layout.apply {
            setColorSchemeResources(R.color.colorBlue)
            setOnRefreshListener { postDelayed( { isRefreshing = false },3000) }
        }
    }

    override fun initData() {

    }

}