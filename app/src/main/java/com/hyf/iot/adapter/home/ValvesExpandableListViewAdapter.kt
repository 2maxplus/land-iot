package com.hyf.iot.adapter.home

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TableRow
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.device.Datas
import java.util.ArrayList

class ValvesExpandableListViewAdapter(
        context: Activity?,
        groups: ArrayList<String>,
        childs: ArrayList<ArrayList<Datas>>
) : BaseExpandableListAdapter() {
    private var context: Activity? = null
    private var groups: ArrayList<String>
    private var childs: ArrayList<ArrayList<Datas>>

    init {
        this.context = context
        this.groups = groups
        this.childs = childs
    }

    //返回一级列表的个数
    override fun getGroupCount(): Int {
        return groups.size
    }

    //返回每个二级列表的个数
    override fun getChildrenCount(groupPosition: Int): Int { //参数groupPosition表示第几个一级列表
        return childs[groupPosition].size
    }

    //返回一级列表的单个item（返回的是对象）
    override fun getGroup(groupPosition: Int): Any {
        return groups[groupPosition]
    }

    //返回二级列表中的单个item（返回的是对象）
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return childs[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    //每个item的id是否是固定？一般为true
    override fun hasStableIds(): Boolean {
        return true
    }

    //【重要】填充一级列表
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group, null)
        }
        val tv_group = convertView!!.findViewById<TextView>(R.id.tv_group)
        tv_group.text = groups[groupPosition]
        return convertView
    }

    //【重要】填充二级列表
    override fun getChildView(
            groupPosition: Int,
            childPosition: Int,
            isLastChild: Boolean,
            convertView: View?,
            parent: ViewGroup
    ): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shebai_list_item, null)
        }

        val info1 = convertView!!.findViewById<TableRow>(R.id.info1)
        info1.visibility = View.VISIBLE
        val text1 = convertView!!.findViewById<TextView>(R.id.tv_air_temperature)
        val text2 = convertView!!.findViewById<TextView>(R.id.tv_air_humidity)
        val text3 = convertView!!.findViewById<TextView>(R.id.tv_soil_temperature)
        val text4 = convertView!!.findViewById<TextView>(R.id.tv_soil_moisture)
        val text5 = convertView!!.findViewById<TextView>(R.id.tv_sun_exposure)
        val text6 = convertView!!.findViewById<TextView>(R.id.tv_device_no)
        val recyclerView = convertView!!.findViewById<RecyclerView>(R.id.recycler_view)

        text1.text = childs[groupPosition][childPosition].str1
        text2.text = childs[groupPosition][childPosition].str2
        text3.text = childs[groupPosition][childPosition].str3
        text4.text = childs[groupPosition][childPosition].str4
        text5.text = childs[groupPosition][childPosition].str5
        text6.text = childs[groupPosition][childPosition].str6
//        recyclerView.apply {
//            this!!.layoutManager = GridLayoutManager(context, 2)
//            adapter = ValveListAdapter(this@ValvesExpandableListViewAdapter.context,
//                childs[groupPosition][childPosition].valves,
//                object : ValveListAdapter.GetCounts {
//                    override fun adds() {
//
//                    }
//                    override fun subs() {
//
//                    }
//
//                })
//        }
        return convertView
    }

    //二级列表中的item是否能够被选中？可以改为true
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}