package com.hyf.iot.adapter.plan

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.hyf.iot.R
import com.hyf.iot.domain.plan.IrrigatePlanGroupInfos
import com.hyf.iot.domain.plan.Plan
import com.hyf.iot.ui.fragment.plans.PlanFragment
import com.hyf.iot.utils.showToast

class PlanExpandableListViewAdapter(context: Context?, var groupList: MutableList<Plan>, var childList: MutableList<IrrigatePlanGroupInfos>) : BaseExpandableListAdapter() {
    private var context: Context? = null
    private var groupData: MutableList<Plan>
    private var childData: MutableList<IrrigatePlanGroupInfos>
    var planId: String = ""

    init {
        this.context = context
        this.groupData = groupList
        this.childData = childList
    }

    //返回一级列表的个数
    override fun getGroupCount(): Int {
        return groupData.size
    }

    //返回每个二级列表的个数
    override fun getChildrenCount(groupPosition: Int): Int { //参数groupPosition表示第几个一级列表
        return childData.size
    }

    //返回一级列表的单个item（返回的是对象）
    override fun getGroup(groupPosition: Int): Plan {
        return groupData[groupPosition]
    }

    //返回二级列表中的单个item（返回的是对象）
    override fun getChild(groupPosition: Int, childPosition: Int): IrrigatePlanGroupInfos {
        return childData[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    //每个item的id是否是固定？一般为true
    override fun hasStableIds(): Boolean {
        return true
    }

    //【重要】填充一级列表
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, view: View?, parent: ViewGroup): View {
        var convertView = view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_plan_group, null)
        }
        val tvGroup = convertView!!.findViewById<TextView>(R.id.tv_plan_name)
        val tvState = convertView.findViewById<TextView>(R.id.tv_plan_state)
        //计划操作
        val ll_operate = convertView.findViewById<LinearLayout>(R.id.ll_operate)
        val rl_operate_2 = convertView.findViewById<RelativeLayout>(R.id.rl_operate_2)
        val btn_operate_1 = convertView.findViewById<ImageView>(R.id.btn_operate_1)
        val btn_operate_2 = convertView.findViewById<ImageView>(R.id.btn_operate_2)

        val item = getGroup(groupPosition)
        tvGroup.text = item.name  //计划名字
        tvState.text = item.stateString  //计划状态
        when (item.state) {
            1 -> {  // 在执行（暂停、停止）
                ll_operate.visibility = View.VISIBLE
                btn_operate_1.setImageResource(R.drawable.icon_pause)
                btn_operate_2.setImageResource(R.drawable.icon_stop)
//                btn_operate_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_pause, 0, 0, 0)
//                btn_operate_1.text = "暂停"
//                btn_operate_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_stop, 0, 0, 0)
//                btn_operate_2.text = "停止"

                btn_operate_1.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        context!!.showToast("无计划可执行")
                    else
                        (context!! as PlanFragment).getPresenter().setPlanSuspend(planId)

                }
                btn_operate_2.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        context!!.showToast("无计划可执行")
                    else
                        (context!! as PlanFragment).getPresenter().setPlanStop(planId)
                }
            }
            3 -> { // 已执行（再次执行）
                ll_operate.visibility = View.VISIBLE
                rl_operate_2.visibility = View.GONE
                btn_operate_1.setImageResource(R.drawable.icon_repeate_exe)
//                btn_operate_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_repeate_exe, 0, 0, 0)
//                btn_operate_1.text = "再次执行"

                btn_operate_1.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        context!!.showToast("无计划可执行")
                    else
                        (context!! as PlanFragment).getPresenter().setPlanStart(planId)
                }
            }
            2 -> { // 已暂停（恢复、停止）
                ll_operate.visibility = View.VISIBLE
                btn_operate_1.setImageResource(R.drawable.icon_recovery)
                btn_operate_2.setImageResource(R.drawable.icon_stop)
//                btn_operate_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_recovery, 0, 0, 0)
//                btn_operate_1.text = "恢复"
//                btn_operate_2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_stop, 0, 0, 0)
//                btn_operate_2.text = "停止"

                btn_operate_1.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        context!!.showToast("无计划可执行")
                    else
                        (context!! as PlanFragment).getPresenter().setPlanContinue(planId)
                }
                btn_operate_2.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        context!!.showToast("无计划可执行")
                    else
                        (context!! as PlanFragment).getPresenter().setPlanStop(planId)
                }
            }
            0 -> { // 未执行
                ll_operate.visibility = View.VISIBLE
                rl_operate_2.visibility = View.GONE
                btn_operate_1.setImageResource(R.drawable.icon_recovery)
//                btn_operate_1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_recovery, 0, 0, 0)
//                btn_operate_1.text = "开始执行"

                btn_operate_1.setOnClickListener {
                    if (planId.isNullOrEmpty())
                        context!!.showToast("无计划可执行")
                    else
                        (context!! as PlanFragment).getPresenter().setPlanStart(planId)
                }
            }
        }

        return convertView
    }

    //【重要】填充二级列表
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n", "WrongConstant")
    override fun getChildView(
            groupPosition: Int,
            childPosition: Int,
            isLastChild: Boolean,
            view: View?,
            parent: ViewGroup
    ): View {
        var convertView = view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_plan_child, null)
        }

        val title = convertView!!.findViewById<TextView>(R.id.tv_title)
        val recyclerView = convertView.findViewById<RecyclerView>(R.id.recycler_view)

        val item = getChild(groupPosition, childPosition)
        title?.text = item.name
        val sAdapter = PlanListAdapter(context!!, item.irrigatePlanGroupControlsInfosRef)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = sAdapter
        }
        return convertView
    }

    //二级列表中的item是否能够被选中？可以改为true
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}