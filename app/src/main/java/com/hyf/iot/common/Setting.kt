package com.hyf.iot.common

import com.hyf.iot.BuildConfig
import com.hyf.iot.utils.SPUtils


/** 服务器Host */
const val DEBUG = false
const val HTTP_API_DOMAIN_DEBUG = "http://yun.land-iot.com:6061"
const val HTTP_API_DOMAIN_RELEASE = "http://yun.land-iot.com:6060"
const val HTTP_API_DOWNLOAD_RELEASE = "${HTTP_API_DOMAIN_RELEASE}/api/Package/Newest?type=0"

object HttpDomain{
    val HTTP_API_DOMAIN = if(DEBUG) HTTP_API_DOMAIN_DEBUG else HTTP_API_DOMAIN_RELEASE
}

object CP{
    var lastPosition: Int
        get() = SPUtils.getInt(Constant.SPConstant.CUR_LAST_POSITION)
        set(value) = SPUtils.putInt(Constant.SPConstant.CUR_LAST_POSITION, value)

    var lastOffset: Int
        get() = SPUtils.getInt(Constant.SPConstant.CUR_LAST_OFFSET)
        set(value) = SPUtils.putInt(Constant.SPConstant.CUR_LAST_OFFSET, value)
    var currentItem: Int
        get() = SPUtils.getInt(Constant.SPConstant.CUR_ITEM)
        set(value) = SPUtils.putInt(Constant.SPConstant.CUR_ITEM, value)
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

