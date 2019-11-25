package com.hyf.iot.adapter.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.hyf.iot.domain.pumb.FrequencyConverterCabinetInfo
import com.hyf.iot.ui.fragment.pumb.FrequencyConverterFragment

class PumpRoomFragmentAdapter(fm: androidx.fragment.app.FragmentManager, var fragmenList: MutableList<FrequencyConverterCabinetInfo>) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    private var isRefresh: Boolean = false

    fun isRefresh(isRefresh: Boolean){
        this.isRefresh = isRefresh
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        val fragments = FrequencyConverterFragment()
        val bundle = Bundle()
        bundle.putParcelable("data",fragmenList[position])
        bundle.putBoolean("isRefresh",isRefresh)
//        bundle.putParcelableArrayList("data",fragmenList[position])
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