package com.hyf.iot.adapter.rv

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.domain.device.SensorValveInfo
import com.hyf.iot.protocol.http.IUserHttpProtocol
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

    init {
        this.context = context
        this.mData = list
    }

    fun setGetOunts(getCounts: GetCounts) {
        this.getCounts = getCounts
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val valve = mData[position]
        holder.tvValveName?.text = valve.name

        when (valve.state) {
            3 ->  // 打开
            {
                holder.switchState?.isChecked = true
                valvesCount++
            }
            2 -> {  //正在启动
                holder.switchState?.isEnabled = false
            }
            0, 1 ->  //关闭
                holder.switchState?.isChecked = false
        }

        holder.switchState?.setOnClickListener {
            val valveState = valve.state
            val state = when (valveState) {
                1 -> "open"
                3 -> "close"
                else -> "open"
            }
            if (state.isEmpty()) {
                context?.showToast("不可操作")
                return@setOnClickListener
            }
            HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                    .setDeviceStateById(state, valve.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { it.data }
                    .subscribeEx(
                            {
                                if (it.success) {
                                    valve.state = 2
                                    holder.switchState.isEnabled = false
                                } else {
                                    context?.showToast(it.message)
                                }
                                notifyDataSetChanged()
                            }, {})
        }

        holder.switchState?.setOnCheckedChangeListener { _, isChecked ->
            if(getCounts == null) return@setOnCheckedChangeListener
            if (isChecked) {
                getCounts.adds()
            } else {
                getCounts.subs()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
            ViewHolders(LayoutInflater.from(context).inflate(R.layout.valve_operate_item, parent, false))


    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val tvValveName = itemview?.findViewById<TextView>(R.id.tv_valve_name)
        val switchState = itemview?.findViewById<Switch>(R.id.switch_state)
    }

    interface GetCounts {
        fun adds()
        fun subs()
    }

    fun getValves(): Int = valvesCount

}