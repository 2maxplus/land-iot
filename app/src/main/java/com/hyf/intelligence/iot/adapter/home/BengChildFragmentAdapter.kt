package com.hyf.intelligence.iot.adapter.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.hyf.intelligence.iot.domain.pumb.WaterPumpValves
import com.hyf.intelligence.iot.fragment.pumb.PumpItemFragment

class BengChildFragmentAdapter(fm: FragmentManager, var fragmentList: MutableList<WaterPumpValves>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragments = PumpItemFragment()

//        val bundle = Bundle()
//        bundle.putSerializable("data",date[position])
//        fragments.arguments = bundle
        return fragments

    }

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentList[position].name
    }

}