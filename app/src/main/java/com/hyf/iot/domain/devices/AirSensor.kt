package com.hyf.iot.domain.devices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AirSensor(
        val airMoisture: Float,
        val airTemperature: Float,
        val created: String,
        val deviceAddress: String,
        val deviceId: String,
        val deviceNo: String,
        val id: String,
        val isDeleted: Boolean,
        val updated: String
) : Parcelable