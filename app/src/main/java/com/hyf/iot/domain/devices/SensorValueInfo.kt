package com.hyf.iot.domain.devices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SensorValueInfo(
    val name: String? = null,
    val unit: String? = null,
    val value: String? = null,
    val valveonly: Boolean
): Parcelable