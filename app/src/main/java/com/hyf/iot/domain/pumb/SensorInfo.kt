package com.hyf.iot.domain.pumb

data class SensorInfo(
    val created: String,
    val creatorId: String,
    val deleted: String,
    val deviceId: String,
    val deviceNo: String,
    val id: String,
    val isDeleted: Boolean,
    val modifierId: String,
    val name: String,
    val originalValue: String,
    val sensorTypeId: String,
    val serialNumber: Int,
    val state: Int,
    val updated: String,
    val value: Int
)