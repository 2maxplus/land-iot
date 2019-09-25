package com.hyf.iot.adapter.plan

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.hyf.iot.common.Constant.KEY_PARAM_STATE
import com.hyf.iot.domain.plan.StateType
import com.hyf.iot.ui.fragment.plan.PlanChildFragment

class PlanFragmentAdapter(fm: FragmentManager, val titleList: ArrayList<StateType>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragments = PlanChildFragment()
        val bundle = Bundle()
        bundle.putString(KEY_PARAM_STATE,titleList[position].state)
        fragments.arguments = bundle
        return fragments

    }

    override fun getCount(): Int = titleList.size

}