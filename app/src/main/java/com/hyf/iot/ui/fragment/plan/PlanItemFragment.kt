package com.hyf.iot.ui.fragment.plan

import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.hyf.iot.R
import com.hyf.iot.adapter.plan.PlanListAdapter
import com.hyf.iot.common.CP
import com.hyf.iot.common.fragment.BaseFragment
import com.hyf.iot.domain.plan.IrrigatePlanGroupInfos
import com.hyf.iot.widget.MyLinearLayoutManager
import kotlinx.android.synthetic.main.layout_recycler_view.*


class PlanItemFragment : BaseFragment() {

    private val mAdapter by lazy { PlanListAdapter(activity!!, mutableListOf()) }

    override fun getLayoutId(): Int = R.layout.layout_recycler_view

    override fun initView() {
        val linearLayoutManager = MyLinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
//        linearLayoutManager.setScrollEnabled(false)
        linearLayoutManager.isSmoothScrollbarEnabled = true

        recycler_view.apply {
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
    }

    override fun initData() {
        super.initData()
        val data = arguments!!.getParcelable<IrrigatePlanGroupInfos>("data") ?: return
        mAdapter.list.addAll(data.irrigatePlanGroupControlsInfosRef)
        mAdapter.notifyDataSetChanged()
        scrollToPosition()
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

