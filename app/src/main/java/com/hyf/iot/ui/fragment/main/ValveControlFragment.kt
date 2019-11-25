package com.hyf.iot.ui.fragment.main

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.adapter.device.ValvesExpandableListViewAdapter
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.fragment.BaseMvpFragment
import com.hyf.iot.contract.MoitureStationContract
import com.hyf.iot.domain.device.MoistureStationMassif
import com.hyf.iot.presenter.MoiturePresenter
import com.hyf.iot.ui.activity.LoginActivity
import com.hyf.iot.ui.activity.ScanActivity
import com.hyf.iot.utils.FSearchTool
import com.hyf.iot.utils.newIntent
import com.hyf.iot.utils.showToast
import com.hyf.iot.widget.PageStateLayout
import com.hyf.iot.widget.PinnedHeaderExpandableListView
import kotlinx.android.synthetic.main.fragment_valve_control.*
import kotlinx.android.synthetic.main.layout_common_page_state.*
import kotlinx.android.synthetic.main.layout_expandable_listview.*


class ValveControlFragment : BaseMvpFragment<MoitureStationContract.IPresenter>(), MoitureStationContract.IView,PinnedHeaderExpandableListView.OnHeaderUpdateListener {

    private val REQUEST_SCAN_SUCCESS = 110

    private var datas: ArrayList<MoistureStationMassif> = arrayListOf()
    private val mAdapter by lazy { ValvesExpandableListViewAdapter(activity, mutableListOf()) }

    override fun getLayoutId(): Int = R.layout.fragment_valve_control

    override fun initView() {

        page_layout.apply {
            setContentView(View.inflate(activity, R.layout.layout_expandable_listview, null))
            setOnPageErrorClickListener { onReload() }
        }

        refresh_layout.apply {
            setColorSchemeResources(R.color.colorBlue)
            setOnRefreshListener { onReload() }
        }

        ivRefresh.setOnClickListener { onReload() }

        expandableListView.apply {
            setAdapter(mAdapter)
            setGroupIndicator(null)
            //只展开一个组
            setOnGroupExpandListener {
                val count = mAdapter.groupCount
                for (i in 0 until count) {
                    if (i != it) {
                        collapseGroup(i)
                    }
                }
            }
            setOnHeaderUpdateListener(this@ValveControlFragment)
            setOnScrollListener(object : AbsListView.OnScrollListener{
                override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                }
                override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                    if (view != null) {
                        getPositionAndOffset()
                    }
                }
            })
        }

        ivScan.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivityForResult(intent, REQUEST_SCAN_SUCCESS)
        }
        search.setOnClickListener {
            doSearch()
        }
//        editText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
//            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    //关闭软键盘
//                    UIUtils.hideKeyboard(editText)
////                    doSearch()
//
//                    return true
//                }
//                return false
//            }
//        })

    }

    private var lastOffset = 0
    private var lastPosition = 0

    /**
     * 记录View当前位置
     */
    private fun getPositionAndOffset() {
        //获取可视的第一个view
        val topView = expandableListView.getChildAt(0)
        if (topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.top  // this.lastOffset = topView.top
            //得到该View的数组位置
            lastPosition = expandableListView.getPositionForView(topView)
        }
    }

    /**
     * 让View滚动到指定位置
     */
    private fun scrollToPosition() {
        if (lastPosition >= 0) {
            expandableListView.smoothScrollToPositionFromTop(lastPosition,lastOffset)
        }
    }

    override fun initData() {
        getPresenter().getMoistureMassifListByFarmId(LoginUser.farmId)
    }

    override fun onTokenExpired(msg: String) {
        activity?.showToast(msg)
        activity?.newIntent<LoginActivity>()
        activity?.finish()
    }

    override fun registerPresenter() = MoiturePresenter::class.java

    private fun doSearch() {
        val searchText = editText.text.toString()
        val tool = FSearchTool(datas, "massifName")
        mAdapter.list.clear()
        mAdapter.list.addAll(tool.searchTasks(searchText) as MutableList<MoistureStationMassif>)
        if (mAdapter.list.size > 0) {
            mAdapter.notifyDataSetChanged()
            expandableListView.setSelectedGroup(0)
            expandableListView.expandGroup(0)
        }
    }

    private fun onReload() {
        page_layout.setPage(PageStateLayout.PageState.STATE_LOADING)
        initData()
    }

    override fun showPage(data: MutableList<MoistureStationMassif>) {
        refresh_layout.isRefreshing = false
        datas.clear()
        datas.addAll(data)
        if (data == null || data.size == 0) {
            page_layout.setPage(PageStateLayout.PageState.STATE_EMPTY)
        } else {
            page_layout.setPage(PageStateLayout.PageState.STATE_SUCCEED)
            mAdapter.list.clear()
            mAdapter.list.addAll(data)
            mAdapter.notifyDataSetChanged()
            expandableListView.expandGroup(0)
        }
        scrollToPosition()
    }

    override fun errorPage(msg: String?) {
        activity?.showToast(msg!!)
        page_layout.setPage(PageStateLayout.PageState.STATE_ERROR)
    }

    override fun updatePinnedHeader(headerView: View, firstVisibleGroupPos: Int) {
        if(firstVisibleGroupPos >= 0) {
            val firstVisibleGroup = mAdapter.getGroup(firstVisibleGroupPos)
            val textView = headerView.findViewById<View>(R.id.tv_group) as TextView
            textView.text = firstVisibleGroup.massifName
        }
    }

    override fun getPinnedHeader(): View {
        val headerView = layoutInflater.inflate(R.layout.item_group, null) as ViewGroup
        headerView.layoutParams = AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT)
        return headerView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SCAN_SUCCESS && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val result = data.getStringExtra("result")
                if (result.isNotEmpty() && result != "null") {
                    editText.setText(result)
                }
            }
        }
    }

}