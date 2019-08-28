package com.hyf.iot.domain.devices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceItem(
        val airSensor: AirSensor? = null,
        val companyId: String? = null,
        val created: String? = null,
        val flowSensor: FlowSensor? = null,
        val id: String? = null,
        val illuminationSensor: IlluminationSensor? = null,
        val isActive: Boolean,
        val isDeleted: Boolean,
        val name: String? = null,
        val number: String? = null,
        val soilSensors: ArrayList<SoilSensor>? = null,
        val cellVoltage: Int,
        val cellVoltageProportion: Float,
        val signalIntensityProportion: Int,
        val state: Int,
        val stateString: String? = null,
        val type: Int,
        val updated: String? = null,
        val valves: ArrayList<Valve>? = null
) : Parcelable