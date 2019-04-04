package com.hyf.intelligence.iot.adapter.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

class MyFragmentAdapter(fm: FragmentManager, private var list: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(arg0: Int): Fragment = list[arg0]

    override fun getCount(): Int = list.size


}