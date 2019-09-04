package com.hyf.iot.adapter.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.hyf.iot.domain.pumb.FrequencyConverterCabinetInfo
import com.hyf.iot.ui.fragment.pumb.FrequencyConverterFragment

class PumpRoomFragmentAdapter(fm: FragmentManager, var fragmenList: MutableList<FrequencyConverterCabinetInfo>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragments = FrequencyConverterFragment()
        val bundle = Bundle()
        bundle.putParcelable("data",fragmenList[position])
//        bundle.putParcelableArrayList("data",fragmenList[position])
        fragments.arguments = bundle
        return fragments
    }

    override fun getCount(): Int = fragmenList.size

    override fun getItemPosition(obj: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItemId(position: Int): Long {
        return System.currentTimeMillis().hashCode().toLong()
    }

}