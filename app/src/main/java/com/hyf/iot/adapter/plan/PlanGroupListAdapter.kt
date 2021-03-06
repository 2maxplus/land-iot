package com.hyf.iot.adapter.plan

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hyf.iot.R
import com.hyf.iot.domain.plan.IrrigatePlanGroupInfos

class PlanGroupListAdapter(context: Activity?, var list : MutableList<IrrigatePlanGroupInfos>) : RecyclerView.Adapter<PlanGroupListAdapter.ViewHolders>() {
    private var context: Activity? = null
    private var layoutId:Int = R.layout.item_plan_child
    private var mData: MutableList<IrrigatePlanGroupInfos>
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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders =
        ViewHolders(LayoutInflater.from(context).inflate(layoutId, parent, false))

    class ViewHolders(itemview: View?) : RecyclerView.ViewHolder(itemview!!) {
        val title = itemview?.findViewById<TextView>(R.id.tv_title)
        val recyclerView = itemview?.findViewById<RecyclerView>(R.id.recycler_view)

    }

}