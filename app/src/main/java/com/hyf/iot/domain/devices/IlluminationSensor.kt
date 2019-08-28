package com.hyf.iot.domain.devices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IlluminationSensor(
        val created: String,
        val deviceId: String,
        val deviceNo: String,
        val id: String,
        val illumination: Int,
        val isDeleted: Boolean,
        val updated: String
): Parcelable