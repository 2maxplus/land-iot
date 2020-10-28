package com.hyf.iot.domain.pumb

data class PumpStation(
    val cellVoltage: Int,
    val companyId: String,
    val created: String,
    val creatorId: String,
    val deleted: String,
    val farmId: String,
    val id: String,
    val isActive: Boolean,
    val isDeleted: Boolean,
    val latitude: Int,
    val latitudeType: Int,
    val longitude: Int,
    val longitudeType: Int,
    val massifId: String,
    val modifierId: String,
    val name: String,
    val number: String,
    val restartCount: Int,
    val sensorInfo: List<SensorInfo>,
    val signalIntensity: Int,
    val state: Int,
    val stateString: String,
    val temperature: Int,
    val type: Int,
    val typeString: String,
    val updated: String
)