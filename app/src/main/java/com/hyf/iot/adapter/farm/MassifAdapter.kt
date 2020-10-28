package com.hyf.iot.adapter.farm

import android.annotation.SuppressLint
import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.farm.Massif
import com.hyf.iot.utils.StringUtils
import com.hyf.iot.widget.findViewByIdEx
import com.hyf.iot.widget.loadmore.LoadMoreRecyclerAdapter

/**
 * 地块
 *
 */
class MassifAdapter(mContext: Activity, mData: MutableList<Massif>) : LoadMoreRecyclerAdapter<Massif>(mContext, mData) {

    private var context: Activity? = null
    init {
        this.context = mContext
    }

    override fun getItemHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder =
            NewsInfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_massif, parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsInfoViewHolder) {
            val item = mData[position]
            holder.title.text = item.name
            holder.size.text = "${StringUtils.get2Decimal(item.size)}亩"
//            Glide
//                    .with(mContext)
//                    .load(item.CoverImageUrl)
//                    .into(holder.ivCoverImage)

        }
    }

    class NewsInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title by lazy { itemView?.findViewByIdEx<TextView>(R.id.tv_massifName) }
        val size by lazy { itemView?.findViewByIdEx<TextView>(R.id.tv_massifSize) }
        val ivCoverImage by lazy { itemView?.findViewByIdEx<ImageView>(R.id.iv_massif) }
    }

}