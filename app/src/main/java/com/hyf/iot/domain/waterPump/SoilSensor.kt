package com.hyf.iot.domain.waterPump

data class SoilSensor(
        val created: String,
        val deviceId: String,
        val deviceNo: String,
        val id: String,
        val isDeleted: Boolean,
        val soilMoisture: Int,
        val soilTemperature: Int,
        val updated: String
)