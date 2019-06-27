package com.hyf.iot.adapter.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.Starred
import com.hyf.iot.widget.findViewByIdEx
import com.hyf.iot.widget.loadmore.LoadMoreRecyclerAdapter

class StarredAdapter(mContext: Context, mData: MutableList<Starred>) : LoadMoreRecyclerAdapter<Starred>(mContext, mData) {

    override fun getItemHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            StarredViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_starred, parent, false))

    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is StarredViewHolder) {
            val item = mData[position]
            holder.tv_project_name.text = item.name
            holder.tv_language.text = item.language
            holder.tv_author.text = item.owner.login
            holder.tv_url.text = item.html_url
            holder.tv_star.text = mContext.getString(R.string.format_star, item.stargazers_count.toString())
            holder.tv_fork.text = mContext.getString(R.string.format_fork, item.forks.toString())
            holder.tv_issues.text = mContext.getString(R.string.format_issues, item.open_issues_count.toString())
            holder.tv_update_time.text = mContext.getString(R.string.format_update, item.updated_at)
            holder.tv_create_time.text = mContext.getString(R.string.format_create, item.created_at)
        }
    }


    class StarredViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_project_name by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_project_name) }
        val tv_language by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_language) }
        val tv_author by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_author) }
        val tv_url by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_url) }
        val tv_star by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_star) }
        val tv_fork by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_fork) }
        val tv_issues by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_issues) }
        val tv_update_time by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_update_time) }
        val tv_create_time by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_create_time) }
    }

}