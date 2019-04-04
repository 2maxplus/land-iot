package com.hyf.intelligence.iot.adapter.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hyf.intelligence.iot.R
import com.hyf.intelligence.iot.domain.Follower
import com.hyf.intelligence.iot.img.ImageLoader
import com.hyf.intelligence.iot.widget.findViewByIdEx
import com.hyf.intelligence.iot.widget.loadmore.LoadMoreRecyclerAdapter
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by L on 2017/7/19.
 */
class FollowersAdapter(mContext: Context, mData: MutableList<Follower>) : LoadMoreRecyclerAdapter<Follower>(mContext, mData) {


    override fun getItemHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            FollowersViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_followers, parent, false))

    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FollowersViewHolder) {
            val item = mData[position]
            ImageLoader.load(mContext, item.avatar_url, holder.iv_avatar,
                    ImageLoader.getRoundRequest(10, RoundedCornersTransformation.CornerType.ALL))
            holder.tv_follower_name.text = item.login
        }
    }


    class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_avatar by lazy { itemView.findViewByIdEx<ImageView>(R.id.iv_avatar) }
        val tv_follower_name by lazy { itemView.findViewByIdEx<TextView>(R.id.tv_follower_name) }
    }

}