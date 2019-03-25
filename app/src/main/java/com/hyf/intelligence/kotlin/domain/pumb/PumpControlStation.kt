package com.hyf.intelligence.kotlin.domain.pumb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PumpControlStation(
        val created: String,
        val deleted: String,
        val deviceNo: String,
        val frequencyConverterCabinet: FrequencyConverterCabinet,
        val id: String,
        val isDeleted: Boolean,
        val name: String,
        val pumpRoomId: String,
        val updated: String
) : Parcelable