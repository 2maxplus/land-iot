package com.hyf.intelligence.iot.adapter.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.hyf.intelligence.iot.domain.pumb.PumpControlStation
import com.hyf.intelligence.iot.fragment.pumb.FrequencyConverterFragment

class BengFragmentAdapter(fm: FragmentManager,var fragmenList: MutableList<PumpControlStation>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragments = FrequencyConverterFragment()
        val bundle = Bundle()
        bundle.putParcelableArrayList("data",fragmenList[position].frequencyConverterCabinet.waterPumpValves)
        fragments.arguments = bundle
        return fragments
    }

    override fun getCount(): Int = fragmenList.size


}