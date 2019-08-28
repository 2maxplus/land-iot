package com.hyf.iot.domain.devices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlowSensor(
        val concurrentFlow: Int,
        val created: String? = null,
        val deviceId: String? = null,
        val deviceNo: String? = null,
        val id: String? = null,
        val isDeleted: Boolean,
        val totalFlow: Int,
        val updated: String? = null
) : Parcelable