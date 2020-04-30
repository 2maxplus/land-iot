package com.hyf.iot.ui.fragment.plan

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyf.iot.R
import com.hyf.iot.adapter.plan.PlanGroupListAdapter
import com.hyf.iot.common.Constant
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.PlanListContract
import com.hyf.iot.domain.plan.Plan
import com.hyf.iot.domain.plan.PlanDetail
import com.hyf.iot.presenter.PlanListPresenter
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.utils.UIUtils
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.PageStateLayout
import kotlinx.android.synthetic.main.layout_common_page_state.*
import kotlinx.android.synthetic.main.layout_verticaltab_recycleview.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView


@SuppressLint("ValidFragment")
class PlanChildFragment : BaseMvpFragment<PlanListContract.IPresenter>(), PlanListContract.IView {

    private var planId: String = ""
    private val mAdapter by lazy { PlanGroupListAdapter(activity, mutableListOf()) }
    private lateinit var planTitleList: MutableList<Plan>

    override fun getLayoutId(): Int = R.layout.layout_common_page_state

    override fun registerPresenter() = PlanListPresenter::class.java

    override fun initView() {
        page_layout.apply {
            setContentView(View.inflate(activity, R.layout.layout_verticaltab_recycleview, null))
            setOnPageErrorClickListener { onReload() }
        }
        tablayout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabView, position: Int) {
                if (planTitleList != null) {
                    planId = planTitleList[position].id
                    getPresenter().getPlanDetail(planId)
                }
            }

            override fun onTabReselected(tab: TabView, position: Int) {
            }
        })

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
        refresh_layout.apply {
            setColorSchemeResources(R.color.colorBlue)
            setOnRefreshListener { onReload() }
        }

        recycler_view.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                refresh_layout.isEnabled = topRowVerticalPosition >= 0
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

//    override fun initData() {
//        val state = arguments!!.getString(Constant.KEY_PARAM_STATE)
//        getPresenter().getPlanList(LoginUser.farmId, state!!)  //LoginUser.farmId
//    }

    override fun onResume() {
        super.onResume()
        onReload()
    }

    fun onReload() {
        page_layout.setPage(PageStateLayout.PageState.STATE_LOADING)
//        initData()
        val state = arguments!!.getString(Constant.KEY_PARAM_STATE)
        getPresenter().getPlanList(LoginUser.farmId, state!!)
    }

    fun setOperateBtnByState(state: String) {
        when (state) {
            "1" -> {  // 在执行（暂停、停止）
                ll_operate.visibility = View.VISIBLE
                btn_operate_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_pause, 0, 0, 0)
                btn_operate_1.text = "暂停"
                btn_operate_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_stop, 0, 0, 0)
                btn_operate_2.text = "停止"

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
            "3" -> { // 已执行（再次执行）
                ll_operate.visibility = View.VISIBLE
                rl_operate_2.visibility = View.GONE
                btn_operate_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_repeate_exe, 0, 0, 0)
                btn_operate_1.text = "再次执行"

                btn_operate_1.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanStart(planId)
                }
            }
            "2" -> { // 已暂停（恢复、停止）
                ll_operate.visibility = View.VISIBLE
                btn_operate_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_recovery, 0, 0, 0)
                btn_operate_1.text = "恢复"
                btn_operate_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_stop, 0, 0, 0)
                btn_operate_2.text = "停止"

                btn_operate_1.setOnClickListener {
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
            "0" -> { // 未执行
                ll_operate.visibility = View.VISIBLE
                rl_operate_2.visibility = View.GONE
                btn_operate_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_recovery, 0, 0, 0)
                btn_operate_1.text = "开始执行"

                btn_operate_1.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        activity!!.showToast("无计划可执行")
                    else
                        getPresenter().setPlanStart(planId)
                }
            }
             "" -> { // 全部
                ll_operate.visibility = View.GONE
            }
        }
    }


    override fun onTokenExpired(msg: String) {
        activity?.showToast(msg)
        activity?.newIntent<LoginActivity>()
        activity?.finish()
    }

    override fun onError(msg: String?) {
        if (msg.isNullOrEmpty()) {
            context?.showToast(R.string.net_error)
        } else {
            context?.showToast(msg)
        }
    }

    override fun onSuccess(msg: String) {
        if (msg.isNotEmpty()) {
            context?.showToast(msg)
            initData()
        }
    }

    override fun showPageList(data: MutableList<Plan>) {
        if (refresh_layout != null)
            refresh_layout.isRefreshing = false
        if (data == null) {
            page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        } else {
            page_layout.setPage(PageStateLayout.PageState.STATE_SUCCEED)
            planTitleList = data
            if (data.size > 0) {
                planId = planTitleList[0].id
                getPresenter().getPlanDetail(planId)
            }else{
                mAdapter.list.clear()
                mAdapter.notifyDataSetChanged()
            }
            tablayout.setTabAdapter(object : TabAdapter {
                override fun getIcon(position: Int): ITabView.TabIcon? {
                    return null
                }

                override fun getBadge(position: Int): ITabView.TabBadge? {
                    return null
                }

                override fun getBackground(position: Int): Int {
                    return 0
                }

                override fun getTitle(position: Int): ITabView.TabTitle {
                    return ITabView.TabTitle.Builder()
                            .setContent(data[position].name)
                            .setTextSize(UIUtils.sp2px(context!!, 5f))
                            .setTextColor(activity!!.resources.getColor(R.color.text_blue), Color.GRAY)
                            .build()
                }

                override fun getCount(): Int {
                    return data.size
                }
            })
        }
    }

    override fun showDetailList(data: PlanDetail) {
        mAdapter.list.clear()
        data.irrigatePlanGroupInfos.sortBy { it.sort } //升序排序
        mAdapter.list.addAll(data.irrigatePlanGroupInfos)
        mAdapter.notifyDataSetChanged()
    }

}