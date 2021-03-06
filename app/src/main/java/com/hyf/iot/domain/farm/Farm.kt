package com.hyf.iot.domain.farm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Farm(
        val address: String? ,
        val companyId: String?,
        val companyName: String?,
        val created: String?,
        val humidity: Double,
        val id: String?,
        val linkMan: String?,
        val linkPhone: String?,
        val isDeleted: Boolean,
        val latitude: Double,
        val latitudeType: Int,
        val longitude: Double,
        val longitudeType: Int,
        val name: String?,
        val rainfall: Double,
        val sunshine: Double,
        val temperature: Double,
        val ultravioletRays: Double,
        val updated: String?,
        val windDirection: Double,
        val windSpeed: Double
) : Parcelable