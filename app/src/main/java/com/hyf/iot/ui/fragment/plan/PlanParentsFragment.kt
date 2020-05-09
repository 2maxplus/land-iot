package com.hyf.iot.ui.fragment.plan

import android.content.Intent
import android.view.View
import com.hyf.iot.R
import com.hyf.iot.adapter.home.PlanParentsFragmentAdapter
import com.hyf.iot.common.Constant
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.PlanParentsContract
import com.hyf.iot.domain.plan.Plan
import com.hyf.iot.presenter.PlanParentsPresenter
import com.hyf.iot.ui.activity.FarmListActivity
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.PageStateLayout
import kotlinx.android.synthetic.main.layout_common_page_state.*
import kotlinx.android.synthetic.main.layout_common_title.*
import kotlinx.android.synthetic.main.layout_common_viewpager.*

class PlanParentsFragment : BaseMvpFragment<PlanParentsContract.IPresenter>(), PlanParentsContract.IView {

    private var isRefresh = false

    override fun onTokenExpired(msg: String) {
        activity?.showToast(msg)
        activity?.newIntent<LoginActivity>()
        activity?.finish()
    }

    override fun registerPresenter() = PlanParentsPresenter::class.java

    override fun getLayoutId(): Int = R.layout.layout_page_state

    private val mAdapter by lazy { PlanParentsFragmentAdapter(childFragmentManager, mutableListOf()) }

    override fun initView() {
        tv_title.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_exchange,0)
        tv_title.setOnClickListener{
            val intent = Intent(context, FarmListActivity::class.java)
            startActivityForResult(intent, Constant.RequestKey.ON_SUCCESS)
        }
        iv_back.visibility = View.INVISIBLE
        tv_operate.visibility = View.VISIBLE
        tv_operate.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_refresh, 0, 0, 0)
        tv_operate.setOnClickListener {
            onReload()
        }
        page_layout.apply {
            setContentView(View.inflate(activity, R.layout.layout_common_viewpager, null))
            setOnPageErrorClickListener { onReload() }
        }
        refresh_layout.apply {
            setColorSchemeResources(R.color.colorBlue)
            setOnRefreshListener {
                onReload()
            }
        }
        viewPager.apply {
            adapter = mAdapter
            currentItem = 0
        }
    }

    override fun onResume() {
        super.onResume()
        tv_title.text = LoginUser.farmName
        onReload()
    }

    private fun onReload() {
        isRefresh = true
        page_layout.setPage(PageStateLayout.PageState.STATE_LOADING)
        getPresenter().getPlanList(LoginUser.farmId,"")
//        initData()
    }

    override fun onError(errorMsg: String?) {
        activity?.showToast(errorMsg!!)
        page_layout.setPage(PageStateLayout.PageState.STATE_ERROR)
    }

    override fun onSuccess(msg: String) {

    }

    override fun showPageList(data: MutableList<Plan>) {
//        refresh_layout.finishRefresh()
        refresh_layout.isRefreshing = false
        mAdapter.fragmenList.clear()
        if (data == null) {
            page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        } else {
            page_layout.setPage(PageStateLayout.PageState.STATE_SUCCEED)
            viewPager.offscreenPageLimit = data.size
            mAdapter.fragmenList.addAll(data)
            mAdapter.notifyDataSetChanged()
        }
        if (childFragmentManager.fragments.size > 0) {
            ((childFragmentManager.fragments[viewPager.currentItem]) as PlanGroupFragment).initData()
        }
    }
}