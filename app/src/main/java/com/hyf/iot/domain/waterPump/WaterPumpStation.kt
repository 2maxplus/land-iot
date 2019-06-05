package com.hyf.iot.domain.waterPump

data class WaterPumpStation(
        val cellVoltage: Int,
        val companyId: String,
        val created: String,
        val id: String,
        val isActive: Boolean,
        val isDeleted: Boolean,
        val latitude: Int,
        val latitudeType: Int,
        val longitude: Int,
        val longitudeType: Int,
        val name: String,
        val number: String,
        val state: Int,
        val temperature: Int,
        val type: Int,
        val updated: String,
        val valveControlDevices: List<ValveControlDevice>
)