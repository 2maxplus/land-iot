package com.hyf.iot.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceCoordinates(
        val cellVoltage: Int,
        val companyId: String,
        val created: String,
        val id: String,
        val isActive: Boolean,
        val isDeleted: Boolean,
        val latitude: Double,
        val latitudeType: Int,
        val longitude: Double,
        val longitudeType: Int,
        val state: Int,
        val temperature: Int,
        val type: Int,
        val updated: String
): Parcelable