package com.hyf.iot.common

/**
 * 常量池
 */
object Constant {

    /**
     *  SharedPreferences常量池
     * */
    object SPConstant {
        val CUR_USER_NAME = "user_name"
        val CUR_TOKEN = "token"
        val CUR_FARM_ID = "farm_id"
    }

    object WEB {
        val SCHEME = "KotlinMVP"
    }

    const val KEY_PARAM_ID = "id"
    const val KEY_PARAM_LAT = "lat"
    const val KEY_PARAM_LONG = "long"
    const val KEY_PARAM_ADDRESS = "address"
    const val KEY_PARAM_NAME = "name"
    const val KEY_PARAM_1 = "param_1"

    object RequestKey {
        val REQUEST_LOGIN_SUCCESS = 10005
        val ON_SUCCESS = 10000
        val REQUEST_CHOOSE = 10002
        val REQUEST_PERMISSION = 10001
        val REQUEST_GPS_CODE = 10010
    }


}