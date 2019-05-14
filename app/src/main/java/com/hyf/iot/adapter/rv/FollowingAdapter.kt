package com.hyf.iot.adapter.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.Following
import com.hyf.iot.img.ImageLoader
import com.hyf.iot.widget.findViewByIdEx
import com.hyf.iot.widget.loadmore.LoadMoreRecyclerAdapter
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by L on 2017/10/9.
 */
class FollowingAdapter(mContext: Context, mData: MutableList<Following>) : LoadMoreRecyclerAdapter<Following>(mContext, mData) {

    override fun getItemHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            FollowingViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_following, parent, false))

    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FollowingViewHolder) {
            val item = mData[position]
            holder.tv_following_name.text = item.login
            ImageLoader.load(mContext, item.avatar_url, holder.iv_avatar,
                    ImageLoader.getRoundRequest(10, RoundedCornersTransformation.CornerType.ALL))
        }
    }


    class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_following_name by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_following_name) }
        val iv_avatar by lazy { itemView.findViewByIdEx<ImageView>(R.id.iv_avatar) }
    }
}