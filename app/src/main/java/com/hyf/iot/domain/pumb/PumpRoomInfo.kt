package com.hyf.iot.domain.pumb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PumpRoomInfo(
    val created: String,
    val deleted: String,
    val farmId: String,
    val id: String,
    val isDeleted: Boolean,
    val name: String,
    val updated: String
): Parcelable