package com.ljb.kt.contract

/**
 * 公共View层契约接口
 */
interface IViewContract{
    fun onTokenExpired(msg: String)
}