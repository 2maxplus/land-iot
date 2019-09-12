package com.hyf.iot.domain.plan

data class IrrigatePlanGroupControlsInfo(
    val deviceId: String,
    val deviceName: String,
    val id: String,
    val irrigatePlanGroupId: String,
    val irrigatePlanId: String,
    val sensorId: String,
    val sensorName: String,
    val deviceState: Int,  //设备状态
    val sensorState: Int, //传感器状态
    val deviceStateString: String,
    val sensorStateString: String,
    val updated: String
)