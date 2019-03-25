package com.hyf.intelligence.kotlin.domain.pumb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WaterPump(
        val created: String,
        val id: String,
        val deleted: String,
        val isDeleted: Boolean,
        val name: String,
        val pumpControlStationDeviceNo: String,
        val pumpControlStationId: String,
        val serialNumber: Int,
        val state: Int,
        val totalFlow: Int,
        val updated: String
) : Parcelable