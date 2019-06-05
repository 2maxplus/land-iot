package com.hyf.iot.domain.waterPump

data class ValveControlDevice(
        val airSensor: AirSensor,
        val cellVoltage: Int,
        val cellVoltageProportion: Int,
        val companyId: String,
        val created: String,
        val id: String,
        val illuminationSensor: IlluminationSensor,
        val isActive: Boolean,
        val isDeleted: Boolean,
        val latitude: Int,
        val latitudeType: Int,
        val longitude: Int,
        val longitudeLatitudeString: String,
        val longitudeType: Int,
        val name: String,
        val number: String,
        val soilSensors: List<SoilSensor>,
        val state: Int,
        val stateString: String,
        val temperature: Int,
        val type: Int,
        val typeString: String,
        val updated: String,
        val valves: List<Valve>
)