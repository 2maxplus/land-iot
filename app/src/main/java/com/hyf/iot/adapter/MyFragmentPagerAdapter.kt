package com.hyf.iot.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.PagerAdapter

import java.util.ArrayList

internal class MyFragmentPagerAdapter(private val fm: androidx.fragment.app.FragmentManager, private var mFragments: ArrayList<androidx.fragment.app.Fragment>?) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return mFragments!![position]
    }

    override fun getCount(): Int {
        return mFragments!!.size
    }

    fun setFragments(fragments: ArrayList<androidx.fragment.app.Fragment>) {
        if (this.mFragments != null) {
            var ft: androidx.fragment.app.FragmentTransaction? = fm.beginTransaction()
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
        return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
    }

}