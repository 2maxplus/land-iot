package com.hyf.iot.ui.fragment.pumb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.hyf.iot.R
import com.hyf.iot.adapter.home.DeviceListAdapter
import com.hyf.iot.adapter.home.ValveListAdapter
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.PumpItemContract
import com.hyf.iot.domain.device.FaKongBean
import com.hyf.iot.domain.device.WaterPump
import com.hyf.iot.presenter.PumpItemPresenter
import com.hyf.iot.widget.MyLinearLayoutManager
import com.hyf.iot.widget.RecycleViewDivider
import com.hyf.iot.widget.dialog.MyDialog
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.android.synthetic.main.pump_item_layout.*


class PumpItemFragment: BaseMvpFragment<PumpItemContract.IPresenter>(),PumpItemContract.IView {

    companion object {
        const val INTENT_ACTION_REFRESH = "com.action.refresh"
    }

    override fun onTokenExpired(msg: String) {
    }

    private val mAdapter by lazy { DeviceListAdapter(activity!!, mutableListOf()) }

    override fun registerPresenter() = PumpItemPresenter::class.java

    override fun showPage(data: MutableList<WaterPump>) {
        mAdapter.list.clear()
        if(data.isNotEmpty() && data.size > 0)
        mAdapter.list.addAll(data[0].valveControlDevices)
        mAdapter.notifyDataSetChanged()
    }

    override fun errorPage(msg: String?) {
    }
    private lateinit var dialogs: MyDialog
    private var content = ""
    private var bengOpenCount = 0  // 阀门已经打开数量

    override fun getLayoutId(): Int = R.layout.pump_item_layout

    override fun initView() {

        val linearLayoutManager = MyLinearLayoutManager(activity!!, LinearLayout.VERTICAL,false)
        linearLayoutManager.setScrollEnabled(false)
        mAdapter.setGetOunts(object : ValveListAdapter.GetCounts {
                        override fun adds() {
                            bengOpenCount ++
                        }
                        override fun subs() {
                            bengOpenCount --
                        }
                    })
        recycler_view.apply {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            addItemDecoration( RecycleViewDivider(
                    activity, LinearLayoutManager.VERTICAL
            ))
        }

        val bean1 = FaKongBean("54m³",3f,9f)
        val bean2 = FaKongBean("66m³",13f,20f)
        val bean3 = FaKongBean("89m³",10f,19f)
        val bean4 = FaKongBean("123m³",4f,18f)
        val bean5 = FaKongBean("36m³",19f,23f)
        val list = ArrayList<FaKongBean>()  // 这里要保证只有7条数据..
        list.add(bean1);list.add(bean2);list.add(bean3);list.add(bean4);list.add(bean5);list.add(bean1);list.add(bean3)
        horizontalChartView.setData(list)

        switchs.setOnClickListener {
            val isCheck = !switchs.isChecked
            content = if (isCheck){
                "当前水泵已经开启20小时，灌溉水量100m3，请问是否关闭水泵?"
            }else{
                if( mAdapter.list.size == 0 ){
                    return@setOnClickListener
                }
                val openCounts = mAdapter.getValves() + bengOpenCount
                if (openCounts > 0){
                    "检测到当前有${openCounts}个阀门已经打开，请问是否继续开泵？"
                }else{
                    "检测到当前没有开启阀门，开泵可能引起管道损坏，请问是否继续开泵？"
                }
            }
            dialogs = MyDialog(activity,content, View.OnClickListener {
                when(it.id){
                    R.id.left_text ->{
                        switchs.isChecked = isCheck
                    }
                    R.id.right_text ->{
                        switchs.isChecked = !isCheck
                    }
                }
                dialogs.dismiss()
            })
            dialogs?.show()
        }

    }

    override fun initData() {
        LoginUser.farmId = "CE4412AB-A62A-446E-8021-235A572FE35B"
        getPresenter().getPumpItemInfo(LoginUser.farmId)
    }

    lateinit var receiveBroadCast: ReceiveBroadCast
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        receiveBroadCast = ReceiveBroadCast()
        val intentFilter = IntentFilter(INTENT_ACTION_REFRESH)
        activity?.registerReceiver(receiveBroadCast,intentFilter)
    }

    override fun onDetach() {
        super.onDetach()
        if(receiveBroadCast != null){
            activity?.unregisterReceiver(receiveBroadCast)
        }
    }

    inner class ReceiveBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            this@PumpItemFragment.initData()
        }
    }

}

