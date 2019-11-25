package com.hyf.iot.adapter.home

import android.os.Bundle
import androidx.core.app.*
import androidx.viewpager.widget.PagerAdapter
import com.hyf.iot.common.CP
import com.hyf.iot.domain.device.WaterPump
import com.hyf.iot.ui.fragment.pumb.PumpItemFragment

class PumpItemFragmentAdapter(private val fm: androidx.fragment.app.FragmentManager, var fragmentList: MutableList<WaterPump>) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
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