package com.hyf.iot.domain.device

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SensorValveInfo(
        val created: String,
        val deviceId: String,
        val deviceNo: String,
        val id: String,
        val isDeleted: Boolean,
        val name: String,
        val serialNumber: Int,
        var state: Int,
        val type: Int,
        val updated: String
): Parcelable