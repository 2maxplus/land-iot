package com.hyf.iot.domain.devices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceInfo(
    val cellVoltage: Int,
    val id: String,
    val isActive: Boolean,
    val latitude: Int,
    val latitudeType: Int,
    val longitude: Int,
    val longitudeType: Int,
    val modifierId: String,
    val name: String? = null,
    val number: String? = null,
    val sensorValueInfos: List<SensorValueInfo>? = null,
    val signalIntensity: Int,
    val state: Int,
    val temperature: Int,
    val type: Int
): Parcelable