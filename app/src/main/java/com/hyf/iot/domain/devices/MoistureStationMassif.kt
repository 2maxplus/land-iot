package com.hyf.iot.domain.devices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoistureStationMassif(
        val valveControlDevices: ArrayList<DeviceItem>? = null,
        val massifId: String,
        val massifName: String
): Parcelable