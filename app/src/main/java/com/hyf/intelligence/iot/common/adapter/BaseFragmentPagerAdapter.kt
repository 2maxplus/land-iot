package com.hyf.intelligence.iot.common.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.view.IBaseView

abstract class BaseFragmentPagerAdapter<out P : IPresenterContract>(fm : FragmentManager) : FragmentPagerAdapter(fm), IBaseView<P> {

}