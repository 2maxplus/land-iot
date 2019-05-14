package com.hyf.iot.domain.device

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceItem(
        val airSensor: AirSensor,
        val companyId: String,
        val created: String,
        val flowSensor: FlowSensor,
        val id: String,
        val illuminationSensor: IlluminationSensor,
        val isActive: Boolean,
        val isDeleted: Boolean,
        val name: String,
        val number: String,
        val soilSensors: ArrayList<SoilSensor>,
        val cellVoltage: Int,
        val cellVoltageProportion: Float,
        val state: Int,
        val stateString: String,
        val type: Int,
        val updated: String,
        val valves: ArrayList<Valve>
) : Parcelable