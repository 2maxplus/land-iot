package com.hyf.iot.adapter.device

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.domain.device.SensorValveInfo
import com.hyf.iot.protocol.http.IUserHttpProtocol
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.ljb.kt.client.HttpFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ValveListAdapter(context: Activity?, list: ArrayList<SensorValveInfo>) :
        RecyclerView.Adapter<ValveListAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var mData: ArrayList<SensorValveInfo>
    private lateinit var getCounts: GetCounts
    private var valvesCount: Int = 0
    private lateinit var holder: ViewHolders

    init {
        this.context = context
        this.mData = list
    }

    fun setGetOunts(getCounts: GetCounts) {
        this.getCounts = getCounts
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        this.holder = holder
        val valve = mData[position]
        holder.tvValveName?.text = valve.name

        when (valve.state) {
            4 ->  // 打开
            {
                holder.switchState?.isChecked = true

                holder.switchOn?.isChecked = true
                holder.switchOff?.isChecked = false
                holder.tvOperateTip?.text = "开"
                holder.tvOperateTip?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                valvesCount++
            }
            1, 3 -> {  //正在启动
                holder.switchState?.isEnabled = false

                holder.switchOn?.isChecked = false
                holder.switchOff?.isChecked = false
                holder.tvOperateTip?.text = "运行中"
                if (1 == valve.state) { //执行关
                    holder.tvOperateTip?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.arrow_right)
                } else if (3 == valve.state) {  //执行开
                    holder.tvOperateTip?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.arrow_left)
                }
            }
            0, 2 ->  //关闭
            {
                holder.switchState?.isChecked = false

                holder.switchOn?.isChecked = false
                holder.switchOff?.isChecked = true
                holder.tvOperateTip?.text = "关"
                holder.tvOperateTip?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
                valvesCount--
            }
        }

        holder.switchOn?.setOnClickListener { valveOperate(valve, "open") }
        holder.switchOff?.setOnClickListener { valveOperate(valve, "close") }
        holder.switchState?.setOnClickListener {
            valveOperate(valve, "")
        }

        holder.switchState?.setOnCheckedChangeListener { _, isChecked ->
            if (getCounts == null) return@setOnCheckedChangeListener
            if (isChecked) {
                getCounts.adds()
            } else {
                getCounts.subs()
            }
        }
        holder.switchOn?.setOnCheckedChangeListener { _, isChecked ->
            if (getCounts == null) return@setOnCheckedChangeListener
            if (isChecked) {
                getCounts.adds()
            }
        }
        holder.switchOff?.setOnCheckedChangeListener { _, isChecked ->
            if (getCounts == null) return@setOnCheckedChangeListener
            if (isChecked) {
                getCounts.subs()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
            ViewHolders(LayoutInflater.from(context).inflate(R.layout.valve_operate_item, parent, false))


    private fun valveOperate(valve: SensorValveInfo, state: String) {
        val valveState = valve.state
//        val state = when (valveState) {
//            2 -> "open"
//            4 -> "close"
//            else -> "open"
//        }
        if (state.isEmpty()) {
            context?.showToast("不可操作")
            return
        }
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .setDeviceStateById(state, valve.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .map { it.data }
                .subscribeEx(
                        {
                            when (it.code) {
                                RESULT_SUCCESS -> {
                                    val data = it.data
                                    if (data.success) {
                                        if (state == "open") {
                                            valve.state = 3  //执行开
                                        } else {
                                            valve.state = 1  //执行关
                                        }
                                        holder.switchState?.isEnabled = false

                                        holder.switchOn?.isChecked = false
                                        holder.switchOff?.isChecked = false
                                        holder.tvOperateTip?.text = "运行中"
                                        if (1 == valve.state) { //执行关
                                            holder.tvOperateTip?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.arrow_right)
                                        } else if (3 == valve.state) {  //执行开
                                            holder.tvOperateTip?.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.arrow_left)
                                        }
                                    } else {
                                        context?.showToast(data.message)
                                    }

                                }
                                214, 215, 216 -> { //重新登陆
                                    App.instance.removeAllActivity()
                                    context!!.newIntent<LoginActivity>()
                                    context!!.finish()
                                }
                                else -> {
                                    context?.showToast(it.msg)
                                }
                            }
                            notifyDataSetChanged()
                        }, {})
    }

    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val tvValveName = itemview?.findViewById<TextView>(R.id.tv_valve_name)
        val switchState = itemview?.findViewById<Switch>(R.id.switch_state)
        val switchOn = itemview?.findViewById<Switch>(R.id.switch_on)
        val tvOperateTip = itemview?.findViewById<TextView>(R.id.tv_operate_tip)
        val switchOff = itemview?.findViewById<Switch>(R.id.switch_off)
    }

    interface GetCounts {
        fun adds()
        fun subs()
    }

    fun getValves(): Int = valvesCount

}