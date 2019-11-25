package com.hyf.iot.common.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.view.IBaseView

abstract class BaseFragmentPagerAdapter<out P : IPresenterContract>(fm : androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm), IBaseView<P> {

}