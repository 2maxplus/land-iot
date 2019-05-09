package com.hyf.intelligence.iot.fragment.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.activity.ValveDetailActivity
import com.hyf.intelligence.iot.activity.ScanActivity
import com.hyf.intelligence.iot.adapter.home.ValvesExpandableListViewAdapter
import com.hyf.intelligence.iot.common.fragment.BaseFragment
import com.hyf.intelligence.iot.domain.device.Datas
import com.hyf.intelligence.iot.domain.device.Valves
import com.hyf.intelligence.iot.utils.newIntent
import kotlinx.android.synthetic.main.fragment_valve_control.*


class ValveControlFragment: BaseFragment() {

    private val REQUEST_SCAN_SUCCESS = 110

    private lateinit var groups : ArrayList<String>
    private lateinit var childs : ArrayList<ArrayList<Datas>>
    override fun getLayoutId(): Int = R.layout.fragment_valve_control

    override fun initView() {

        groups = ArrayList()
        groups.add("类别1");groups.add("类别2");groups.add("类别3");groups.add("类别4")

        var valves = ArrayList<Valves>()
        valves.add(Valves("","阀门1",0))
        valves.add(Valves("","阀门2",1))
        valves.add(Valves("","阀门3",1))
        valves.add(Valves("","阀门4",0))

        val datas1 = Datas("","12°C", "33%", "43°C", "82%", "34Lux", "1736QSDFVB817362",valves)
        val datas2 = Datas("","22°C", "23%", "53°C", "32%", "23Lux", "173612ADSAS9A362",valves)
        val datas3 = Datas("","15°C", "53%", "73°C", "22%", "43Lux", "17362537IUJHG241",valves)
        val datas4 = Datas("","11°C", "43%", "23°C", "62%", "54Lux", "173ERTGHJ9817131",valves)
        val datas5 = Datas("","42°C", "16%", "21°C", "32%", "12Lux", "17362UYGBNMJG131",valves)
        val datas6 = Datas("","62°C", "17%", "43°C", "62%", "16Lux", "1736KJNMKK192131",valves)
        val datas7 = Datas("","22°C", "14%", "26°C", "39%", "78Lux", "1736257TYJNBVCD1",valves)
        val datas8 = Datas("","18°C", "15%", "83°C", "30%", "10Lux", "1736UHBNJKUHGFD1",valves)
        val datas9 = Datas("","16°C", "18%", "33°C", "33%", "19Lux", "1736ADFQ867U7131",valves)
        val datas10 = Datas("","62°C", "23%", "23°C","12%", "12Lux", "173876T368OI6718",valves)
        val datas11 = Datas("","22°C", "45%", "13°C","22%", "20Lux", "173645345RDF9131",valves)
        val datas12 = Datas("","24°C", "41%", "23°C","52%", "10Lux", "173621298UJ09451",valves)


        val array1 = ArrayList<Datas>()
        array1.add(datas1);array1.add(datas2);array1.add(datas3)

        val array2 = ArrayList<Datas>()
        array2.add(datas4);array2.add(datas5);array2.add(datas6)

        val array3 = ArrayList<Datas>()
        array3.add(datas7);array3.add(datas8);array3.add(datas9)

        val array4 = ArrayList<Datas>()
        array4.add(datas10);array4.add(datas11);array4.add(datas12)

        childs = ArrayList()
        childs.add(array1);childs.add(array2);childs.add(array3);childs.add(array4)

        expandableListView.apply {
            setAdapter(ValvesExpandableListViewAdapter(activity,groups,childs))
            setGroupIndicator(null)
            setOnChildClickListener(object : ExpandableListView.OnChildClickListener{
                override fun onChildClick(parent: ExpandableListView?, v: View?, groupPosition: Int, childPosition: Int, id: Long): Boolean {
                    val bundle = Bundle()
                    bundle.putString("id",childs[groupPosition][childPosition].id)  //设备ID
                    activity?.newIntent<ValveDetailActivity>(bundle)
                    return true
                }
            })
        }

        ivScan.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivityForResult(intent,REQUEST_SCAN_SUCCESS)
        }
        search.setOnClickListener {
            val searchText = editText.text.toString()
            when {
                searchText.contains("1") -> {
                    expandableListView.setSelectedGroup(0)
                    expandableListView.expandGroup(0)
                }
                searchText.contains("2") -> {
                    expandableListView.setSelectedGroup(1)
                    expandableListView.expandGroup(1)
                }
                searchText.contains("3") -> {
                    expandableListView.setSelectedGroup(2)
                    expandableListView.expandGroup(2)
                }
                searchText.contains("4") -> {
                    expandableListView.setSelectedGroup(3)
                    expandableListView.expandGroup(3)
                }
            }

        }

        refresh_layout.apply {
            setColorSchemeResources(R.color.colorBlue)
            setOnRefreshListener { postDelayed( { isRefreshing = false },2000) }
        }

    }

    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        expandableListView.expandGroup(0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_SCAN_SUCCESS && resultCode == Activity.RESULT_OK){
            if(data != null){
                val result = data.getStringExtra("result")
                if(result.isNotEmpty() && result != "null"){
                    editText.setText(result)
                }
            }
        }
    }

}