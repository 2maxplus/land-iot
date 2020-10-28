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
import kotlinx.android.synthetic.main.layout_plan_group.tabLayout
import kotlinx.android.synthetic.main.layout_plan_group.viewPager

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
                    post { currentItem = p  }

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
        tv_plan_state.text = data.stateString
        setOperateBtnByState(data.state)

        planId = data.id
        getPresenter().getPlanDetail(planId)


    }

    private fun setOperateBtnByState(state: Int) {
        when (state) {
            0 -> { // 未执行 （执行）
                rl_operate_1.isEnabled = false
                rl_operate_2.isEnabled = false
                rl_operate_3.isEnabled = false
                rl_operate_4.isEnabled = true

                btn_operate_4.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanStart(planId)
                }
            }
            1 -> {  // 在执行（暂停、停止）
                rl_operate_1.isEnabled = true
                rl_operate_2.isEnabled = true
                rl_operate_3.isEnabled = false
                rl_operate_4.isEnabled = false

                btn_operate_1.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanSuspend(planId)

                }
                btn_operate_2.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanStop(planId)
                }
            }

            2 -> { // 已暂停（恢复、停止）
                rl_operate_1.isEnabled = false
                rl_operate_2.isEnabled = true
                rl_operate_3.isEnabled = true
                rl_operate_4.isEnabled = false

                btn_operate_3.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanContinue(planId)
                }
                btn_operate_2.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanStop(planId)
                }
            }
            3 -> { // 已完成（再次执行）
                rl_operate_1.isEnabled = false
                rl_operate_2.isEnabled = false
                rl_operate_3.isEnabled = false
                rl_operate_4.isEnabled = true

                btn_operate_4.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanStart(planId)
                }
            }
            4 -> { //已终止（再次执行）
                rl_operate_1.isEnabled = false
                rl_operate_2.isEnabled = false
                rl_operate_3.isEnabled = false
                rl_operate_4.isEnabled = true

                btn_operate_4.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanStart(planId)
                }
            }
            5 -> { //待执行 （停止）
                rl_operate_1.isEnabled = false
                rl_operate_2.isEnabled = true
                rl_operate_3.isEnabled = false
                rl_operate_4.isEnabled = false

                btn_operate_2.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanStop(planId)
                }
            }
        }
    }

    override fun onTokenExpired(msg: String) {  }

    override fun onError(errorMsg: String?) {
        if (errorMsg.isNullOrEmpty()) {
            context?.showToast(R.string.net_error)
        } else {
            context?.showToast(errorMsg)
        }
    }

    override fun onSuccess(msg: String) {
        if (msg.isNotEmpty()) {
            context?.showToast(msg)
            initData()
        }
    }

    override fun showDetailList(data: PlanDetail) {
        try {
            mAdapter.fragmentList.clear()
//            viewPager.offscreenPageLimit = data.irrigatePlanGroupInfos.size
            data.irrigatePlanGroupInfos.sortBy { it.sort } //升序排序
            mAdapter.fragmentList.addAll(data.irrigatePlanGroupInfos)
            mAdapter.notifyDataSetChanged()
//            if (childFragmentManager.fragments.size > 0) {
//                ((childFragmentManager.fragments[viewPager.currentItem]) as PlanItemFragment).initData()
//            }
        } catch (e : Exception) {
            print(e)
        }
    }

}