package com.hyf.iot.ui.fragment.plan

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
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
//import kotlinx.android.synthetic.main.layout_refresh_recycler_view.*
import kotlinx.android.synthetic.main.layout_verticaltab_recycleview.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView


@SuppressLint("ValidFragment")
class PlanChildFragment : BaseMvpFragment<PlanListContract.IPresenter>(), PlanListContract.IView {

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
                if (planTitleList != null)
                    getPresenter().getPlanDetail(planTitleList[position].id)
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

    }

    override fun initData() {
        val state = arguments!!.getString(Constant.KEY_PARAM_STATE)
        getPresenter().getPlanList(LoginUser.farmId, state!!)  //LoginUser.farmId
    }

    fun setOperateBtnByState(state: String){
        when(state){
            "1" -> {  // 在执行（暂停、停止）
                ll_operate.visibility = View.VISIBLE

            }
            "3" -> { // 已执行（再次执行）
                ll_operate.visibility = View.VISIBLE

            }
            "2" -> { // 已暂停（恢复、停止）
                ll_operate.visibility = View.VISIBLE

            }
            "0","" -> { // 未执行，全部
                ll_operate.visibility = View.GONE
            }
        }
    }

    fun onReload() {
        page_layout.setPage(PageStateLayout.PageState.STATE_LOADING)
        initData()
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

    override fun showPageList(data: MutableList<Plan>) {
        refresh_layout.isRefreshing = false
        if (data == null) {
            page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        } else {
            page_layout.setPage(PageStateLayout.PageState.STATE_SUCCEED)
            planTitleList = data
            if (data.size > 0)
                getPresenter().getPlanDetail(data[0].id)
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
                            .setTextSize(UIUtils.sp2px(context!!,5f))
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
        mAdapter.list.addAll(data.irrigatePlanGroupInfos)
        mAdapter.notifyDataSetChanged()
    }

}