package com.hyf.iot.common

import com.hyf.iot.BuildConfig
import com.hyf.iot.utils.SPUtils


/** 服务器Host */
const val DEBUG = false
const val HTTP_API_DOMAIN_DEBUG = "http://192.168.0.140:8030"
const val HTTP_API_DOMAIN_RELEASE = "http://yun.land-iot.com:6060"
const val HTTP_API_DOWNLOAD_RELEASE = "${HTTP_API_DOMAIN_RELEASE}/api/Package/Newest?type=0"

object HttpDomain{
    val HTTP_API_DOMAIN = if(DEBUG) HTTP_API_DOMAIN_DEBUG else HTTP_API_DOMAIN_RELEASE
}

/** Log */
val LOG_DEBUG = BuildConfig.DEBUG
const val RESULT_SUCCESS = 0

/** 用户登录信息 */
class LoginUser {
    companion object {
        var name: String
            get() = SPUtils.getString(Constant.SPConstant.CUR_USER_NAME)
            set(value) = SPUtils.putString(Constant.SPConstant.CUR_USER_NAME, value)

        var token: String
            get() = SPUtils.getString(Constant.SPConstant.CUR_TOKEN)
            set(value) = SPUtils.putString(Constant.SPConstant.CUR_TOKEN, value)

        var farmId: String
            get() = SPUtils.getString(Constant.SPConstant.CUR_FARM_ID)
            set(value) = SPUtils.putString(Constant.SPConstant.CUR_FARM_ID, value)
    }
}

