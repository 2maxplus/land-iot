package com.ljb.kt.contract

/**
 * 公共View层契约接口
 * Created by L on 2017/7/10.
 */
interface IViewContract{
    fun onTokenExpired(msg: String)
}