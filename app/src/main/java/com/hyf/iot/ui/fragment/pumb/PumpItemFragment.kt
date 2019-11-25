package com.hyf.iot.ui.fragment.pumb

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.hyf.iot.App
import com.hyf.iot.R
import com.hyf.iot.adapter.device.DeviceAdapter
import com.hyf.iot.adapter.device.ValveListAdapter
import com.hyf.iot.common.CP
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.common.fragment.BaseFragment
import com.hyf.iot.domain.device.FaKongBean
import com.hyf.iot.domain.device.WaterPump
import com.hyf.iot.protocol.http.IUserHttpProtocol
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.MyLinearLayoutManager
import com.hyf.iot.widget.RecycleViewDivider
import com.hyf.iot.widget.chart.HorizontalChartViewFlow
import com.hyf.iot.widget.dialog.EditDialog
import com.hyf.iot.widget.dialog.MyDialog
import com.hyf.iot.widget.findViewByIdEx
import com.ljb.kt.client.HttpFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_recycler_view.*


class PumpItemFragment : BaseFragment() {

    private val mAdapter by lazy { DeviceAdapter(activity!!, mutableListOf()) }

    private lateinit var dialogs: MyDialog
    private lateinit var editDialog: EditDialog
    private var content = ""
    private var bengOpenCount = 0  // 阀门已经打开数量
    private var waterPump : WaterPump? = null

    override fun getLayoutId(): Int = R.layout.layout_recycler_view

    override fun initView() {
        val linearLayoutManager = MyLinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
//        linearLayoutManager.setScrollEnabled(false)
        linearLayoutManager.isSmoothScrollbarEnabled = true

        mAdapter.setGetOunts(object : ValveListAdapter.GetCounts {
            override fun adds() {
                bengOpenCount++
            }

            override fun subs() {
                bengOpenCount--
            }
        })
        mAdapter.isShowLoadMore(false)
        recycler_view.apply {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            //监听RecyclerView滚动状态
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (recyclerView.layoutManager != null) {
                        getPositionAndOffset()
                    }
                }

                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
                    val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount === 0) 0 else recyclerView.getChildAt(0).top
                    (parentFragment!!.parentFragment as PumpRoomFragment).getonScroll(topRowVerticalPosition >= 0)
                }
            })
        }
    }

    override fun initData() {
        super.initData()
        waterPump = arguments!!.getParcelable("data") ?: return
        when {
            waterPump!!.serialNumber == 3  -> {   //潜水泵
                val header = LayoutInflater.from(mContext).inflate(R.layout.layout_switch, null)
                mAdapter.setHeaderView(header)
                val switch = header.findViewByIdEx<Switch>(R.id.switch_state)
                when(waterPump!!.state){
                    0,2 -> {  //关闭
                        switch.isChecked = false
                    }
                    4 -> {  //打开
                        switch.isChecked = true
                    }
                }
            }
            else -> {
                val header = LayoutInflater.from(mContext).inflate(R.layout.layout_pump_item, null)
                mAdapter.setHeaderView(header)
                initHeaderView(header)
            }
        }
        mAdapter.mData.clear()
        if (waterPump!!.deviceInfos != null && waterPump!!.deviceInfos!!.size > 0)
            mAdapter.mData.addAll(waterPump!!.deviceInfos!!)
        mAdapter.notifyDataSetChanged()
        scrollToPosition()
    }

    private var horizontalChartView: HorizontalChartViewFlow? = null
    private var switchs: Switch? = null
    private var tvPipePressure: TextView? = null
    private var tvConstantPressure: TextView? = null
    private var tvPressureSet: TextView? = null
    private fun initHeaderView(header: View) {
        horizontalChartView = header.findViewByIdEx(R.id.horizontalChartView)
        switchs = header.findViewByIdEx(R.id.switchs)
        tvPipePressure = header.findViewByIdEx(R.id.tv_pipePressure)
        tvConstantPressure = header.findViewByIdEx(R.id.tv_constantPressure)
        tvPressureSet = header.findViewByIdEx(R.id.tv_pressure_set)

//        val bean1 = FaKongBean("54m³", 3f, 9f)
//        val bean2 = FaKongBean("66m³", 13f, 20f)
//        val bean3 = FaKongBean("89m³", 10f, 19f)
//        val bean4 = FaKongBean("123m³", 4f, 18f)
//        val bean5 = FaKongBean("36m³", 19f, 23f)
//        val list = ArrayList<FaKongBean>()  // 这里要保证只有7条数据..
//        list.add(bean1);list.add(bean2);list.add(bean3);list.add(bean4);list.add(bean5);list.add(bean1);list.add(bean3)
//        horizontalChartView!!.setData(list)

        switchs!!.setOnClickListener {
            val isCheck = !switchs!!.isChecked
            content = if (isCheck) {
                "当前水泵已经开启0小时，灌溉水量0m3，请问是否关闭水泵?"
            } else {
                if (mAdapter.mData.size == 0) {
                    return@setOnClickListener
                }
                val openCounts = mAdapter.getValves() + bengOpenCount
                if (openCounts > 0) {
                    "检测到当前有${openCounts}个阀门已经打开，请问是否继续开泵？"
                } else {
                    "检测到当前没有开启阀门，开泵可能引起管道损坏，请问是否继续开泵？"
                }
            }
            dialogs = MyDialog(activity, content, View.OnClickListener {
                when (it.id) {
                    R.id.left_text -> {
                        switchs!!.isChecked = isCheck
                    }
                    R.id.right_text -> {
                        switchs!!.isChecked = !isCheck
                    }
                }
                dialogs.dismiss()
            })
            dialogs.show()
        }

        tvPressureSet!!.setOnClickListener {
            editDialog = EditDialog(activity, "压力值设置",content, object : EditDialog.OnPublicInputBoxClickListener {
                override fun inputTextContent(editText: EditText) {
                    val pressureText = editText.text.toString()
                    setPressure(waterPump!!.deviceId!!,pressureText)
                }
            })
            editDialog.show()
        }
//        getValueByDeviceId()
    }


    @SuppressLint("SetTextI18n")
    private fun getValueByDeviceId(){
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getValueByDeviceId(waterPump!!.deviceId!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when (it.code) {
                                RESULT_SUCCESS -> {
                                    tvConstantPressure!!.text = "恒压值:${it.data}"
                                }
                                214, 215, 216 -> { //重新登陆
                                    App.instance.removeAllActivity()
                                    activity!!.newIntent<LoginActivity>()
                                    activity!!.finish()
                                }
                                else -> {
                                    context?.showToast(it.msg)
                                }
                            }
                        }, {})
    }

    private fun setPressure(id: String,pressureText: String){
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .setPressureByDeviceId(id, pressureText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when (it.code) {
                                RESULT_SUCCESS -> {
                                    context?.showToast("设置成功")
                                }
                                214, 215, 216 -> { //重新登陆
                                    App.instance.removeAllActivity()
                                    activity!!.newIntent<LoginActivity>()
                                    activity!!.finish()
                                }
                                else -> {
                                    context?.showToast(it.msg)
                                }
                            }
                        }, {})
    }

    /**
     * 记录RecyclerView当前位置
     */
    private fun getPositionAndOffset() {
        val layoutManager = recycler_view.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
        //获取可视的第一个view
        val topView = layoutManager.getChildAt(0)
        if (topView != null) {
            //获取与该view的顶部的偏移量
            CP.lastOffset = topView.top  // this.lastOffset = topView.top
            //得到该View的数组位置
            CP.lastPosition = layoutManager.getPosition(topView) // lastPosition = layoutManager.getPosition(topView)
        }
    }

    /**
     * 让RecyclerView滚动到指定位置
     */
    private fun scrollToPosition() {
        if (recycler_view.layoutManager != null && CP.lastPosition >= 0) {
            (recycler_view.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).scrollToPositionWithOffset(CP.lastPosition, CP.lastOffset)
//            (recycler_view.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(2, -129)
        }
    }
}

