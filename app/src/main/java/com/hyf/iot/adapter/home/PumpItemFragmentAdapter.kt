package com.hyf.iot.adapter.home

import android.os.Bundle
import android.support.v4.app.*
import android.support.v4.view.PagerAdapter
import com.hyf.iot.domain.device.WaterPump
import com.hyf.iot.ui.fragment.pumb.PumpItemFragment

class PumpItemFragmentAdapter(private val fm: FragmentManager, var fragmentList: MutableList<WaterPump>) : FragmentPagerAdapter(fm) {

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

    override fun getItemPosition(obj: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItemId(position: Int): Long {
        return System.currentTimeMillis().hashCode().toLong()
    }


}