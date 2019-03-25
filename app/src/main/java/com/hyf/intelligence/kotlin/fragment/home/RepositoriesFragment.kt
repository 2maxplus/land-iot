package com.hyf.intelligence.kotlin.fragment.home

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.act.WebActivity
import com.hyf.intelligence.kotlin.adapter.rv.RepositoriesAdapter
import com.hyf.intelligence.kotlin.common.fragment.BaseMvpFragment
import com.hyf.intelligence.kotlin.contract.RepositoriesContract
import com.hyf.intelligence.kotlin.domain.Repository
import com.hyf.intelligence.kotlin.presenter.RepositoriesPresenter
import com.hyf.intelligence.kotlin.widget.PageStateLayout.PageState
import com.hyf.intelligence.kotlin.widget.loadmore.LoadMoreRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_repos.*
import kotlinx.android.synthetic.main.layout_refresh_recycler_view.*

/**
 * Repos page
 */
class RepositoriesFragment : BaseMvpFragment<RepositoriesContract.IPresenter>(), RepositoriesContract.IView,
        LoadMoreRecyclerAdapter.LoadMoreListener, LoadMoreRecyclerAdapter.OnItemClickListener {

    private val mAdapter by lazy { RepositoriesAdapter(activity!!, mutableListOf()) }

    override fun getLayoutId() = R.layout.fragment_repos

    override fun registerPresenter() = RepositoriesPresenter::class.java

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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
            mAdapter.setOnLoadMoreListener(this@RepositoriesFragment)
            mAdapter.setOnItemClickListener(this@RepositoriesFragment)
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

    override fun showPage(data: MutableList<Repository>, page: Int) {
        if (page == 1) {
            refresh_layout.isRefreshing = false
            if (data.isEmpty()) {
                page_layout.setPage(PageState.STATE_EMPTY)
            } else {
                page_layout.setPage(PageState.STATE_SUCCEED)
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