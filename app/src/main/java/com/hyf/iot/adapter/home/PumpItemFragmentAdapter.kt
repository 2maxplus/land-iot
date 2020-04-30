package com.hyf.iot.adapter.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
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
        return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
    }

    override fun getItemId(position: Int): Long {
        return System.currentTimeMillis().hashCode().toLong()
    }




}