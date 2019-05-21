package com.hyf.iot.common.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.view.IBaseView

abstract class BaseFragmentPagerAdapter<out P : IPresenterContract>(fm : FragmentManager) : FragmentPagerAdapter(fm), IBaseView<P> {

}