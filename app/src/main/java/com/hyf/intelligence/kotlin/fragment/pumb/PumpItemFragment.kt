package com.hyf.intelligence.kotlin.fragment.pumb

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.adapter.home.FaKongListAdapter
import com.hyf.intelligence.kotlin.adapter.home.ValveListAdapter
import com.hyf.intelligence.kotlin.common.fragment.BaseFragment
import com.hyf.intelligence.kotlin.domain.device.Datas
import com.hyf.intelligence.kotlin.domain.device.FaKongBean
import com.hyf.intelligence.kotlin.domain.device.Valves
import com.hyf.intelligence.kotlin.widget.RecycleViewDivider
import com.hyf.intelligence.kotlin.widget.dialog.MyDialog
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.android.synthetic.main.pump_item_layout.*

class PumpItemFragment: BaseFragment() {

    private lateinit var faList : ArrayList<Datas>
    private lateinit var adapter: FaKongListAdapter
    private lateinit var dialogs: MyDialog
    private var content = ""
    private var bengOpenCount = 0  // 阀门已经打开数量

    override fun getLayoutId(): Int = R.layout.pump_item_layout

    override fun initView() {

        var valves = ArrayList<Valves>()
        valves.add(Valves("","阀门1",0))
        valves.add(Valves("","阀门2",0))
        valves.add(Valves("","阀门3",0))
        valves.add(Valves("","阀门4",1))

        val datas1 = Datas("12°C", "33%", "43°C", "82%", "34Lux", "1736QSDFVB817362",valves)
        val datas2 = Datas("22°C", "23%", "53°C", "32%", "23Lux", "173612ADSAS9A362",valves)
        val datas3 = Datas("15°C", "53%", "73°C", "22%", "43Lux", "17362537IUJHG241",valves)
        val datas4 = Datas("11°C", "43%", "23°C", "62%", "54Lux", "173ERTGHJ9817131",valves)
        val datas5 = Datas("42°C", "16%", "21°C", "32%", "12Lux", "17362UYGBNMJG131",valves)
        val datas6 = Datas("62°C", "17%", "43°C", "62%", "16Lux", "1736KJNMKK192131",valves)
        val datas7 = Datas("22°C", "14%", "26°C", "39%", "78Lux", "1736257TYJNBVCD1",valves)
        val datas8 = Datas("18°C", "15%", "83°C", "30%", "10Lux", "1736UHBNJKUHGFD1",valves)
        val datas9 = Datas("16°C", "18%", "33°C", "33%", "19Lux", "1736ADFQ867U7131",valves)
        val datas10 = Datas("62°C","23%", "23°C", "12%", "12Lux", "173876T368OI6718",valves)
        val datas11 = Datas("22°C","45%", "13°C", "22%", "20Lux", "173645345RDF9131",valves)
        val datas12 = Datas("24°C","41%", "23°C", "52%", "10Lux", "173621298UJ09451",valves)

        faList = ArrayList()
        faList.add(datas1);faList.add(datas2);faList.add(datas3)
        faList.add(datas4);faList.add(datas5);faList.add(datas6)
        faList.add(datas7);faList.add(datas8);faList.add(datas9)
        faList.add(datas10);faList.add(datas11);faList.add(datas12)

        recycler_view.addItemDecoration(
            RecycleViewDivider(
                activity, LinearLayoutManager.VERTICAL
            )
        )
        recycler_view.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL,false)
        adapter = FaKongListAdapter(
            activity,
            R.layout.shebai_list_item,
            faList,
            object : ValveListAdapter.GetCounts {
                override fun adds() {
                    bengOpenCount ++
                }

                override fun subs() {
                    bengOpenCount --
                }

            })
        recycler_view.adapter = adapter


        val bean1 = FaKongBean("54m³",3f,9f)
        val bean2 = FaKongBean("66m³",13f,20f)
        val bean3 = FaKongBean("89m³",10f,19f)
        val bean4 = FaKongBean("123m³",4f,18f)
        val bean5 = FaKongBean("36m³",19f,23f)
        val list = ArrayList<FaKongBean>()// 这里要保证只有7条数据..
        list.add(bean1);list.add(bean2);list.add(bean3);list.add(bean4);list.add(bean5);list.add(bean1);list.add(bean3)
        horizontalChartView.setData(list)

        switchs.setOnClickListener {
            val ischecked = !switchs.isChecked
            content = if (ischecked){
                "当前水泵已经开启20小时，灌溉水量100m3，请问是否关闭水泵?"
            }else{
                var openCounts = adapter.getValves() + bengOpenCount
                if (openCounts > 0){
                    "检测到当前有${openCounts}个阀门已经打开，请问是否继续开泵？"
                }else{
                    "检测到当前没有开启阀门，开泵可能引起管道损坏，请问是否继续开泵？"
                }
            }
            dialogs = MyDialog(activity,"提示",content, View.OnClickListener {
                when(it.id){
                    R.id.left_text ->{
                        switchs.isChecked = ischecked
                    }
                    R.id.right_text ->{
                        switchs.isChecked = !ischecked
                    }
                }
                dialogs.dismiss()
            })
            dialogs.show()
        }

    }

    override fun initData() {

    }

}