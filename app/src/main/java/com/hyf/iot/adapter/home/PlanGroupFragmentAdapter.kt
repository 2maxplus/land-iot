package com.hyf.iot.adapter.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.hyf.iot.domain.plan.IrrigatePlanGroupInfos
import com.hyf.iot.ui.fragment.plan.PlanItemFragment

class PlanGroupFragmentAdapter(fm: FragmentManager, var fragmentList: MutableList<IrrigatePlanGroupInfos>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragments = PlanItemFragment()
        val bundle = Bundle()
        bundle.putParcelable("data",fragmentList[position])
        fragments.arguments = bundle
        return fragments
    }

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentList[position].name
    }

    override fun getItemPosition(obj: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItemId(position: Int): Long {
        return System.currentTimeMillis().hashCode().toLong()
    }




}