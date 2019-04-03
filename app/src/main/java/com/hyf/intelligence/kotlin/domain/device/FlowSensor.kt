package com.hyf.intelligence.kotlin.domain.device

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlowSensor(
        val concurrentFlow: Int,
        val created: String,
        val deviceId: String,
        val deviceNo: String,
        val id: String,
        val isDeleted: Boolean,
        val totalFlow: Int,
        val updated: String
) : Parcelable