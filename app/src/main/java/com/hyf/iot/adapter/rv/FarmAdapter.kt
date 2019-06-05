package com.hyf.iot.adapter.rv

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.common.Constant
import com.hyf.iot.common.Constant.RequestKey.ON_SUCCESS
import com.hyf.iot.common.LoginUser
import com.hyf.iot.domain.farm.Farm
import com.hyf.iot.ui.activity.FarmDetailActivity
import com.hyf.iot.utils.newIntent
import com.hyf.iot.widget.findViewByIdEx


/**
 *
 * */
class FarmAdapter(context: Activity?, list: MutableList<Farm>) : RecyclerView.Adapter<FarmAdapter.ViewHolders>() {
    private var context: Activity? = null
    var mData: MutableList<Farm>
    //这个是checkbox的Hashmap集合
    private var map: HashMap<Int, Boolean> = hashMapOf()

    private lateinit var mCallback: Callback
    fun setCallback(callback: Callback) {
        this.mCallback = callback
    }

    fun setData(mData: MutableList<Farm>) {
        this.mData.clear()
        this.mData.addAll(mData)
        if (mData.size == 1) LoginUser.farmId = mData[0].id!!
        for (i in 0 until mData.size) {
            map[i] = LoginUser.farmId == mData[i].id
        }
    }

    init {
        this.context = context
        this.mData = list
    }

    override fun getItemCount(): Int = mData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        holder.tvFarmName?.text = item.name
        holder.checkbox?.isChecked = map[position]!!
        holder.checkbox?.setOnClickListener {
            map[position] = !map[position]!!
            notifyDataSetChanged()
            singleSet(position)
            mCallback.click(it, item.id!!)
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constant.KEY_PARAM_ID, item.id)
            context?.newIntent<FarmDetailActivity>(ON_SUCCESS,bundle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
            ViewHolders(LayoutInflater.from(context).inflate(R.layout.farm_item, parent, false))


    class ViewHolders(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val checkbox by lazy { itemView?.findViewByIdEx<CheckBox>(R.id.checkbox) }
        val tvFarmName by lazy { itemView?.findViewByIdEx<TextView>(R.id.tv_farm_name) }
    }

    interface Callback {
        fun click(v: View, id: String)
    }

    /**
     * 单选
     *
     * @param position
     */
    private fun singleSet(position: Int) {
        val entries = map.entries
        for (entry in entries) {
            entry.setValue(false)
        }
        map[position] = true
        notifyDataSetChanged()
    }

    fun getCheckedId(): String {
        var id = ""
        map.forEach { (key, value) ->
            if (value) id = mData[key].id!!
        }
        return id
    }

}