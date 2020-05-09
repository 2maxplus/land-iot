package com.hyf.iot.adapter.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hyf.iot.common.Constant.KEY_PARAM_STATE
import com.hyf.iot.domain.plan.Plan
import com.hyf.iot.ui.fragment.plan.PlanGroupFragment

class PlanParentsFragmentAdapter(fm: FragmentManager, val fragmenList: MutableList<Plan>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragments = PlanGroupFragment()
        val bundle = Bundle()
        bundle.putParcelable("data",fragmenList[position])
        fragments.arguments = bundle
        return fragments

    }

    override fun getCount(): Int = fragmenList.size

    override fun getItemPosition(obj: Any): Int {
        return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
    }

    override fun getItemId(position: Int): Long {
        return System.currentTimeMillis().hashCode().toLong()
    }
}