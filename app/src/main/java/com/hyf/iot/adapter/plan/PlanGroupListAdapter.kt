package com.hyf.iot.adapter.plan

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hyf.iot.R
import com.hyf.iot.domain.plan.IrrigatePlanGroupInfo

class PlanGroupListAdapter(context: Activity?, var list : MutableList<IrrigatePlanGroupInfo>) : androidx.recyclerview.widget.RecyclerView.Adapter<PlanGroupListAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId:Int = R.layout.item_plan_group
    private var mData: MutableList<IrrigatePlanGroupInfo>
    private var mAdapter: PlanListAdapter? = null

    init {
        this.context = context
        this.mData = list
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        val item = mData[position]
        holder.title?.text = item.name
        mAdapter = PlanListAdapter(context, item.irrigatePlanGroupControlsInfosRef)
        holder.recyclerView?.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
        ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))

    class ViewHolders(itemview: View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemview!!) {
        val title = itemview?.findViewById<TextView>(R.id.tv_title)
        val recyclerView = itemview?.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)

    }

}