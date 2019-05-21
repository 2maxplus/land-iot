package com.hyf.iot.domain

import com.baidu.mapapi.model.LatLng

data class MyLatLng(var latLng: LatLng?, var state: Int) {
    companion object {
        val ABLE = 1
        val UNABLE = 0
    }
}