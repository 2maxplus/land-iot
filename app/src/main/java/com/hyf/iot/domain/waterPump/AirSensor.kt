package com.hyf.iot.domain.waterPump

data class AirSensor(
        val airMoisture: Int,
        val airTemperature: Int,
        val created: String,
        val deviceAddress: String,
        val deviceId: String,
        val deviceNo: String,
        val id: String,
        val isDeleted: Boolean,
        val updated: String
)