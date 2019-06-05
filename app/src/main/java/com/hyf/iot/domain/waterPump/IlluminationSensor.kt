package com.hyf.iot.domain.waterPump

data class IlluminationSensor(
        val created: String,
        val deviceId: String,
        val deviceNo: String,
        val id: String,
        val illumination: Int,
        val isDeleted: Boolean,
        val updated: String
)