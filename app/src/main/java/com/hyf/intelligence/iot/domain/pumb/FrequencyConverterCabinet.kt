package com.hyf.intelligence.iot.domain.pumb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FrequencyConverterCabinet(
        val created: String,
        val currentA: Int,
        val currentB: Int,
        val currentC: Int,
        val id: String,
        val deleted: String,
        val isDeleted: Boolean,
        val name: String,
        val operatorUserId: String,
        val pumpControlStationId: String,
        val totalFlow: Int,
        val totalOutputPower: Int,
        val updated: String,
        val voltageA: Int,
        val voltageB: Int,
        val voltageC: Int,
        val waterPumpValves: ArrayList<WaterPumpValves>
) : Parcelable
