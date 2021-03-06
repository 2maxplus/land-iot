package com.hyf.iot.domain.device

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceInfo(
//        val cellVoltage: Float,
        val id: String,
        val isActive: Boolean,
        val latitude: Double,
        val latitudeType: Int,
        val longitude: Double,
        val longitudeType: Int,
        val modifierId: String,
        val name: String? = null,
        val number: String? = null,
        val sensor_OtherInfos: ArrayList<SensorOtherInfo>? = null,
        val sensor_SignalInfo: SensorSignalInfo? = null,
        val sensor_ValveInfos: ArrayList<SensorValveInfo>? = null,
        val sensor_VoltageInfo: SensorVoltageInfo? = null,
//        val Sensor_WaterPumpInfos:
        val signalIntensity: Int,
        val state: Int,
        val stateString: String,
//        val temperature: Int,
        val type: Int
):Parcelable