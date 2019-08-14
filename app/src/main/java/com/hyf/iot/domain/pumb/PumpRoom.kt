package com.hyf.iot.domain.pumb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PumpRoom(
    val frequencyConverterCabinetInfos: List<FrequencyConverterCabinetInfo>,
    val pumpRoomInfo: PumpRoomInfo
): Parcelable