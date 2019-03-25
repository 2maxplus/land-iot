package com.hyf.intelligence.kotlin.act

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.adapter.home.ValveListAdapter
import com.hyf.intelligence.kotlin.common.act.BaseActivity
import com.hyf.intelligence.kotlin.domain.device.FaKongBean
import com.hyf.intelligence.kotlin.domain.device.Valves
import kotlinx.android.synthetic.main.fakong_datails_layout.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_recycler_view.*

class FakongDatailsActivity: BaseActivity() {
    override fun getLayoutId(): Int = R.layout.fakong_datails_layout

    override fun initSaveInstanceState(saveInstanceState: Bundle?) {
    }

    override fun initView() {

        iv_back.setOnClickListener { finish() }
        tv_title.text = "阀控详情"
        val bean1 = FaKongBean("6H",3f,9f)
        val bean2 = FaKongBean("8H",1f,8f)
        val bean3 = FaKongBean("9H",10f,19f)
        val bean4 = FaKongBean("4H",12f,16f)
        val bean5 = FaKongBean("3H",19f,22f)
        val bean6 = FaKongBean("9H",14f,23f)
        val list = ArrayList<FaKongBean>()
        list.add(bean1);list.add(bean4)

        val list2 = ArrayList<FaKongBean>()
        list2.add(bean3)

        val list3 = ArrayList<FaKongBean>()
        list3.add(bean2);list3.add(bean6)

        val list4 = ArrayList<FaKongBean>()
        list4.add(bean1);list4.add(bean5)

        val lists = ArrayList<ArrayList<FaKongBean>>()
        lists.add(list);lists.add(list2);lists.add(list3);lists.add(list4)

        horizontalChartView.setData(lists)

        var valves = ArrayList<Valves>()
        valves.add(Valves("","阀门1",0))
        valves.add(Valves("","阀门2",0))
        valves.add(Valves("","阀门3",0))
        valves.add(Valves("","阀门4",1))
        recycler_view.apply {
            this!!.layoutManager = GridLayoutManager(context, 2)
            adapter = ValveListAdapter(this@FakongDatailsActivity, valves, object : ValveListAdapter.GetCounts {
                override fun adds() {
                }
                override fun subs() {

                }

            })
        }
    }

    override fun initData() {
    }

}