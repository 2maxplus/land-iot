package com.hyf.intelligence.kotlin.adapter.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.hyf.intelligence.kotlin.domain.pumb.WaterPump
import com.hyf.intelligence.kotlin.fragment.pumb.PumpItemFragment

class BengChildFragmentAdapter(fm: FragmentManager, var fragmentList: MutableList<WaterPump>) : FragmentPagerAdapter(fm) {

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