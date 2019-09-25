package com.hyf.iot.domain.device

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WaterPump(
        val deviceInfos: ArrayList<DeviceInfo>? = null,
        val cellVoltage: Int,
        val companyId: String,
        val id: String,
        val isActive: Boolean,
        val latitude: Int,
        val latitudeType: Int,
        val longitude: Int,
        val longitudeType: Int,
        val name: String,
        val number: String,
        val signalIntensity: Int,
        val state: Int,
        val temperature: Int,
        val type: Int,
        val serialNumber: Int,  //3 = 潜水泵
        val deviceId: String? = ""
): Parcelable