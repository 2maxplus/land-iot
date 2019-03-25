package com.hyf.intelligence.kotlin.adapter.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.hyf.intelligence.kotlin.fragment.home.PlanChildFragment

class PlanFragmentAdapter(fm: FragmentManager, datas: ArrayList<String>) : FragmentPagerAdapter(fm) {

    val data = datas
    override fun getItem(position: Int): Fragment {
        val fragments = PlanChildFragment(position)
//        val bundle = Bundle()
//        bundle.putSerializable("data",date[position])
//        fragments.arguments = bundle
        return fragments

    }

    override fun getCount(): Int = data.size


}