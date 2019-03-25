package com.hyf.intelligence.kotlin.common

import com.hyf.intelligence.kotlin.BuildConfig
import com.hyf.intelligence.kotlin.utils.SPUtils


/** 服务器Host */
//const val HTTP_API_DOMAIN = "https://api.github.com"
const val HTTP_API_DOMAIN = "http://192.168.0.114:5002"

/** Log */
val LOG_DEBUG = BuildConfig.DEBUG

/** 用户登录信息 */
class LoginUser {
    companion object {
        var name: String
            get() = SPUtils.getString(Constant.SPConstant.CUR_USER_NAME)
            set(value) = SPUtils.putString(Constant.SPConstant.CUR_USER_NAME, value)
    }
}

