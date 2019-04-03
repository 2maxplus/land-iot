package com.hyf.intelligence.kotlin.domain.device

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoilSensor(
        val created: String,
        val deviceId: String,
        val deviceNo: String,
        val id: String,
        val isDeleted: Boolean,
        val soilMoisture: Float,
        val soilTemperature: Float,
        val updated: String
) : Parcelable