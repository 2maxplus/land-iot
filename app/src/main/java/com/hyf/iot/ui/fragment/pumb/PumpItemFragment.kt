package com.hyf.iot.ui.fragment.pumb

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import com.hyf.iot.R
import com.hyf.iot.adapter.device.DeviceAdapter
import com.hyf.iot.adapter.device.ValveListAdapter
import com.hyf.iot.common.CP
import com.hyf.iot.common.fragment.BaseFragment
import com.hyf.iot.domain.device.FaKongBean
import com.hyf.iot.domain.device.WaterPump
import com.hyf.iot.widget.MyLinearLayoutManager
import com.hyf.iot.widget.RecycleViewDivider
import com.hyf.iot.widget.chart.HorizontalChartViewFlow
import com.hyf.iot.widget.dialog.MyDialog
import com.hyf.iot.widget.findViewByIdEx
import kotlinx.android.synthetic.main.layout_recycler_view.*
import kotlinx.android.synthetic.main.pump_item_layout.*


class PumpItemFragment : BaseFragment() {

    private val mAdapter by lazy { DeviceAdapter(activity!!, mutableListOf()) }

    private lateinit var dialogs: MyDialog
    private var content = ""
    private var bengOpenCount = 0  // 阀门已经打开数量

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
        val header = LayoutInflater.from(mContext).inflate(R.layout.pump_item_layout, null)
        mAdapter.setHeaderView(header)
        mAdapter.isShowLoadMore(false)
        initHeaderView(header)
        recycler_view.apply {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            addItemDecoration(RecycleViewDivider(
                    activity, LinearLayoutManager.VERTICAL
            ))
            //监听RecyclerView滚动状态
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (recyclerView.layoutManager != null) {
                        getPositionAndOffset()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)

                    val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount === 0) 0 else recyclerView.getChildAt(0).top
                    (parentFragment!!.parentFragment as PumpRoomFragment).getonScroll(topRowVerticalPosition >= 0)
                }
            })
        }
//        scrollView.apply {
//            setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
//                (parentFragment!!.parentFragment as PumpRoomFragment).getonScroll(scrollView.scrollY <= 0)
//            }
//        }

        val bean1 = FaKongBean("54m³", 3f, 9f)
        val bean2 = FaKongBean("66m³", 13f, 20f)
        val bean3 = FaKongBean("89m³", 10f, 19f)
        val bean4 = FaKongBean("123m³", 4f, 18f)
        val bean5 = FaKongBean("36m³", 19f, 23f)
        val list = ArrayList<FaKongBean>()  // 这里要保证只有7条数据..
        list.add(bean1);list.add(bean2);list.add(bean3);list.add(bean4);list.add(bean5);list.add(bean1);list.add(bean3)
        horizontalChartView!!.setData(list)

        switchs!!.setOnClickListener {
            val isCheck = !switchs!!.isChecked
            content = if (isCheck) {
                "当前水泵已经开启20小时，灌溉水量100m3，请问是否关闭水泵?"
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

    }

    override fun initData() {
        super.initData()
        val waterPump = arguments!!.getParcelable<WaterPump>("data") ?: return
        mAdapter.mData.clear()
        if (waterPump.deviceInfos != null && waterPump.deviceInfos.size > 0)
            mAdapter.mData.addAll(waterPump.deviceInfos)
        mAdapter.notifyDataSetChanged()
        scrollToPosition()
    }

    private var horizontalChartView: HorizontalChartViewFlow? = null
    private var switchs: Switch? = null
    private fun initHeaderView(header: View) {
        horizontalChartView = header.findViewByIdEx<HorizontalChartViewFlow>(R.id.horizontalChartView)
        switchs = header.findViewByIdEx<Switch>(R.id.switchs)
    }

    /**
     * 记录RecyclerView当前位置
     */
    private fun getPositionAndOffset() {
        val layoutManager = recycler_view.layoutManager as LinearLayoutManager
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
            (recycler_view.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(CP.lastPosition, CP.lastOffset)
//            (recycler_view.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(2, -129)
        }
    }
}

