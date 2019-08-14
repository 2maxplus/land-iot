package com.hyf.iot.adapter.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.hyf.iot.domain.device.WaterPump
import com.hyf.iot.ui.fragment.pumb.PumpItemFragment

class BengChildFragmentAdapter(fm: FragmentManager, var fragmentList: MutableList<WaterPump>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragments = PumpItemFragment()
        val bundle = Bundle()
        bundle.putParcelable("data",fragmentList[position])
        fragments.arguments = bundle
        return fragments

    }

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentList[position].name
    }

}