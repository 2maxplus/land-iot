package com.hyf.iot.domain.waterPump

data class Valve(
        val created: String,
        val deviceId: String,
        val deviceNo: String,
        val id: String,
        val isDeleted: Boolean,
        val name: String,
        val serialNumber: Int,
        val state: Int,
        val type: Int,
        val updated: String
)