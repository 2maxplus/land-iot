package com.hyf.iot.domain.pumb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PumpControlStations(
        val companyId: String,
        val created: String,
        val deleted: String,
        val id: String,
        val isDeleted: Boolean,
        val name: String,
        val pumpControlStations: List<PumpControlStation>,
        val updated: String
)  :Parcelable