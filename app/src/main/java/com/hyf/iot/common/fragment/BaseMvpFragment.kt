package com.hyf.iot.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.view.MvpFragment

abstract class BaseMvpFragment<out P : IPresenterContract> : MvpFragment<P>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    protected open fun init(savedInstanceState: Bundle?) {}
    protected abstract fun getLayoutId(): Int
    protected open fun initView() {}
    public open fun initData() {}

}