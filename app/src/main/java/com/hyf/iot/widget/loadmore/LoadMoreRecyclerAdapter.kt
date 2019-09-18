package com.hyf.iot.widget.loadmore

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hyf.iot.R

/**
 *
 */
abstract class LoadMoreRecyclerAdapter<T>(val mContext: Context, var mData: MutableList<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val PAGE_DATA_SIZE = 30
        private val TYPE_LOAD_MORE = 0
        private val TYPE_ITEM = 1
        private val TYPE_HEADER = 2
    }

    private var mHeaderView: View? = null
    private var mHeaderHolder: HeaderHolder? = null
    private var mLoadMoreHolder: LoadMoreHolder? = null
    private var isShowLoadMore = true

    private var isLoading = false
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mLoadMoreListener: LoadMoreListener? = null

    override fun getItemId(position: Int) = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_LOAD_MORE) {  //加载更多
            if (mLoadMoreHolder == null) {
                val loadView = LayoutInflater.from(mContext).inflate(R.layout.layout_load_more, parent, false)
                mLoadMoreHolder = LoadMoreHolder(loadView, this)
            }
            initLoadStatusForSize(mData)
            return mLoadMoreHolder!!
        }
        if (viewType == TYPE_HEADER) { //添加header
            mHeaderHolder = HeaderHolder(mHeaderView!!)
            return mHeaderHolder!!
        }
        return getItemHolder(parent, viewType)
    }

    abstract fun getItemHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_LOAD_MORE -> loadMore()
            TYPE_HEADER -> addHeader()
            TYPE_ITEM -> {
                val position1 = getRealPosition(holder)
                onBindData(holder, position1)
                //设置点击事件
                mOnItemClickListener?.apply {
                    holder.itemView.setOnClickListener {
                        this.onItemClick(holder.itemView, position1)
                    }
                }
            }
        }
    }

    abstract fun onBindData(holder: RecyclerView.ViewHolder, position: Int)

    override fun getItemCount(): Int = if (isShowLoadMore || mHeaderView != null) mData.size + 1 else mData.size  //

    override fun getItemViewType(position: Int): Int {
        if (isShowLoadMore) {
            if (itemCount - 1 == position) {
                return TYPE_LOAD_MORE
            }
        }
        if (mHeaderView == null) return TYPE_ITEM
        return if (position == 0) TYPE_HEADER else TYPE_ITEM
    }

    fun onError() {
        isLoading = false
        mLoadMoreHolder?.setStatus(LoadMoreHolder.LoadMoreType.Error)
    }

    private fun initLoadStatusForSize(data: List<T>) {
        if (data.size < PAGE_DATA_SIZE) {
            setLoadMoreStatus(LoadMoreHolder.LoadMoreType.NoMore)
        } else {
            setLoadMoreStatus(LoadMoreHolder.LoadMoreType.LoadMore)
        }
    }

    private fun addHeader() {

    }

    private fun setLoadMoreStatus(status: LoadMoreHolder.LoadMoreType) {
        isLoading = false
        mLoadMoreHolder?.setStatus(status)
    }

    fun setHeaderView(headerView: View) {
        mHeaderView = headerView
        notifyItemInserted(0)
    }

    fun getHeaderView(): View {
        return mHeaderView!!
    }

    fun onNoMore() {
        setLoadMoreStatus(LoadMoreHolder.LoadMoreType.NoMore)
    }

    private fun getRealPosition(holder: RecyclerView.ViewHolder): Int {
        val position = holder.layoutPosition
        return if (mHeaderView == null) position else position - 1
    }


    /**
     *  1、创建加载更多View时触发
     *  2、点击重新加载时触发
     * */
    fun loadMore() {
        if (!isLoading && mLoadMoreListener != null && mLoadMoreHolder!!.getType() == LoadMoreHolder.LoadMoreType.LoadMore) {
            isLoading = true
            mLoadMoreListener!!.onLoadMore()
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setOnLoadMoreListener(listener: LoadMoreListener) {
        mLoadMoreListener = listener
    }

    fun isShowLoadMore(isShow: Boolean) {
        isShowLoadMore = isShow
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }


}