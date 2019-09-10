package com.hyf.iot.domain.plan

data class IrrigatePlanGroupControlsInfo(
    val deviceId: String,
    val deviceName: String,
    val id: String,
    val irrigatePlanGroupId: String,
    val irrigatePlanId: String,
    val sensorId: String,
    val sensorName: String
)