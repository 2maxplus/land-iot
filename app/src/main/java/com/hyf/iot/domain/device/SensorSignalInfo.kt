package com.hyf.iot.domain.device

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SensorSignalInfo(
    val deviceId: String,
    val deviceNo: String,
    val id: String,
    val name: String,
    val originalValue: String,
    val sensorTypeId: String,
    val serialNumber: Int,
    val state: Int,
    val value: Int
): Parcelable