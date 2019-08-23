package com.hyf.iot.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.PagerAdapter

import java.util.ArrayList

internal class MyFragmentPagerAdapter(private val fm: FragmentManager, private var mFragments: ArrayList<Fragment>?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return mFragments!![position]
    }

    override fun getCount(): Int {
        return mFragments!!.size
    }

    fun setFragments(fragments: ArrayList<Fragment>) {
        if (this.mFragments != null) {
            var ft: FragmentTransaction? = fm.beginTransaction()
            for (f in this.mFragments!!) {
                ft!!.remove(f)
            }
            ft!!.commit()
            ft = null
            fm.executePendingTransactions()
        }
        this.mFragments = fragments
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}