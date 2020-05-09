package com.hyf.iot.ui.fragment.plan

import android.annotation.SuppressLint
import androidx.viewpager.widget.ViewPager
import com.hyf.iot.R
import com.hyf.iot.adapter.home.PlanGroupFragmentAdapter
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.PlanChildContract
import com.hyf.iot.domain.plan.Plan
import com.hyf.iot.domain.plan.PlanDetail
import com.hyf.iot.presenter.PlanChildPresenter
import com.hyf.iot.utils.showToast
import kotlinx.android.synthetic.main.layout_plan_group.*


@SuppressLint("ValidFragment")
class PlanGroupFragment : BaseMvpFragment<PlanChildContract.IPresenter>(), PlanChildContract.IView {

    private var planId: String = ""
    private val mAdapter by lazy { PlanGroupFragmentAdapter(childFragmentManager, mutableListOf()) }

    override fun getLayoutId(): Int = R.layout.layout_plan_group

    override fun registerPresenter() = PlanChildPresenter::class.java

    override fun initView() {
        viewPager.apply {
            adapter = mAdapter
            currentItem = 0
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                }

                override fun onPageSelected(p: Int) {
                    currentItem = p
                }
            })
        }
        tabLayout.apply {
            setupWithViewPager(viewPager)
        }

//        val linearLayout = tabLayout.getChildAt(0) as LinearLayout
//        linearLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
//        linearLayout.dividerDrawable = ContextCompat.getDrawable(context!!,
//                R.drawable.layout_divider_vertical)
    }


    override fun initData() {
        val data = arguments!!.getParcelable<Plan>("data") ?: return
        //更新计划基本信息（样式：卡片）
        tv_pumpRoomName.text = data.pumpRoomName
        tv_frequencyConverterCabinetName.text = data.frequencyConverterCabinetName
        tv_waterPumpSensorName.text = data.waterPumpSensorName
        tv_planName.text = data.name
        planId = data.id
        getPresenter().getPlanDetail(planId)
    }

    override fun onTokenExpired(msg: String) {  }

    override fun onError(errorMsg: String?) {
        if (errorMsg.isNullOrEmpty()) {
            context?.showToast(R.string.net_error)
        } else {
            context?.showToast(errorMsg)
        }
    }

    override fun showDetailList(data: PlanDetail) {
        mAdapter.fragmentList.clear()
        data.irrigatePlanGroupInfos.sortBy { it.sort } //升序排序
        mAdapter.fragmentList.addAll(data.irrigatePlanGroupInfos)
        mAdapter.notifyDataSetChanged()
    }

}