package com.hyf.iot.domain.plan

data class Plan(
    val deviceId: String,
    val frequency: String,
    val frequencyConverterCabinetName: String,
    val id: String,
    val name: String,
    val sensorId: String,
    val startTime: String,
    val state: Int,
    val stateString: String,
    val waterPumpSensorName: String
)