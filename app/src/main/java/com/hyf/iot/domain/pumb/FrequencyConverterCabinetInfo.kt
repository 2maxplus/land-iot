package com.hyf.iot.domain.pumb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FrequencyConverterCabinetInfo(
    val created: String,
    val currentA: Int,
    val currentB: Int,
    val currentC: Int,
    val deleted: String,
    val id: String,
    val isDeleted: Boolean,
    val name: String,
    val pumpRoomId: String,
    val totalFlow: Int,
    val totalOutputPower: Int,
    val updated: String,
    val voltageA: Int,
    val voltageB: Int,
    val voltageC: Int
): Parcelable