package com.hyf.intelligence.iot.fragment.home

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.activity.WebActivity
import com.hyf.intelligence.iot.adapter.rv.FollowersDecoration
import com.hyf.intelligence.iot.adapter.rv.FollowingAdapter
import com.hyf.intelligence.iot.common.fragment.BaseMvpFragment
import com.hyf.intelligence.iot.contract.FollowingContract
import com.hyf.intelligence.iot.domain.Following
import com.hyf.intelligence.iot.presenter.FollowingPresenter
import com.hyf.intelligence.iot.widget.PageStateLayout.PageState
import com.hyf.intelligence.iot.widget.loadmore.LoadMoreRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.layout_refresh_recycler_view.*

/**
 * Created by L on 2017/7/18.
 */
class FollowingFragment : BaseMvpFragment<FollowingContract.IPresenter>(), FollowingContract.IView,
        LoadMoreRecyclerAdapter.LoadMoreListener, LoadMoreRecyclerAdapter.OnItemClickListener {
    override fun onTokenExpired(msg: String) {

    }

    private val mAdapter by lazy { FollowingAdapter(activity!!, mutableListOf()) }

    override fun getLayoutId() = R.layout.fragment_following

    override fun registerPresenter() = FollowingPresenter::class.java

    override fun initView() {
        page_layout.apply {
            setContentView(View.inflate(activity, R.layout.layout_refresh_recycler_view, null))
            setOnPageErrorClickListener { onReload() }
        }
        refresh_layout.apply {
            setColorSchemeResources(R.color.colorBlue)
            setOnRefreshListener { getPresenter().onRefresh() }
        }
        recycler_view.apply {
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(FollowersDecoration())
            adapter = mAdapter
            mAdapter.setOnLoadMoreListener(this@FollowingFragment)
            mAdapter.setOnItemClickListener(this@FollowingFragment)
        }
    }

    override fun initData() {
        getPresenter().onRefresh()
    }

    override fun onLoadMore() {
        getPresenter().onLoadMore()
    }

    private fun onReload() {
        page_layout.setPage(PageState.STATE_LOADING)
        getPresenter().onRefresh()
    }

    override fun showPage(data: MutableList<Following>, page: Int) {
        if (page == 1) {
            refresh_layout.isRefreshing = false
            if (data.isEmpty()) {
                page_layout.setPage(PageState.STATE_EMPTY)
            } else {
                page_layout.setPage(PageState.STATE_SUCCEED)
                mAdapter.apply { }
                mAdapter.mData.clear()
                mAdapter.mData.addAll(data)
                mAdapter.initLoadStatusForSize(data)
                mAdapter.notifyDataSetChanged()
            }
        } else {
            if (data.isEmpty()) {
                mAdapter.onNoMore()
            } else {
                mAdapter.mData.addAll(data)
                mAdapter.initLoadStatusForSize(data)
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun errorPage(t: Throwable, page: Int) {
        if (page == 1) {
            page_layout.setPage(PageState.STATE_ERROR)
        } else {
            mAdapter.onError()
        }
    }

    override fun onItemClick(view: View, position: Int) {
        val itemData = mAdapter.mData[position]
        WebActivity.startActivity(activity!!, itemData.html_url)
    }


}