package com.hyf.iot.ui.fragment.plan

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
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
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import kotlinx.android.synthetic.main.layout_refresh_recycler_view.*
import kotlinx.android.synthetic.main.layout_verticaltab_recycleview.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView


@SuppressLint("ValidFragment")
class PlanChildFragment : BaseMvpFragment<PlanListContract.IPresenter>(), PlanListContract.IView {

    private val mAdapter by lazy { PlanGroupListAdapter(activity, mutableListOf()) }
    private lateinit var planTitleList: MutableList<Plan>

    override fun getLayoutId(): Int = R.layout.layout_verticaltab_recycleview

    override fun registerPresenter() = PlanListPresenter::class.java

    override fun initView() {
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
            setOnRefreshListener { initData() }
        }
    }

    override fun initData() {
        val state = arguments!!.getString(Constant.KEY_PARAM_STATE)
        getPresenter().getPlanList("A19CA6B1-3625-4034-BBC6-DAF07838E605", state!!)  //LoginUser.farmId
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
        if (data != null) {
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