package com.hyf.intelligence.iot.common

import com.hyf.intelligence.iot.BuildConfig
import com.hyf.intelligence.iot.utils.SPUtils


/** 服务器Host */
const val HTTP_API_DOMAIN_DEBUG = "http://192.168.0.140:6070"
const val HTTP_API_DOMAIN_RELEASE = "http://app.land-iot.com:6070"

/** Log */
val LOG_DEBUG = BuildConfig.DEBUG
val RESULT_SUCCESS = 0

/** 用户登录信息 */
class LoginUser {
    companion object {
        var name: String
            get() = SPUtils.getString(Constant.SPConstant.CUR_USER_NAME)
            set(value) = SPUtils.putString(Constant.SPConstant.CUR_USER_NAME, value)

        var token: String
            get() = SPUtils.getString(Constant.SPConstant.CUR_TOKEN)
            set(value) = SPUtils.putString(Constant.SPConstant.CUR_TOKEN, value)
    }
}

