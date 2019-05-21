package com.hyf.iot.domain.farm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Farm(
        val id: String,
        val name: String,
        val checked: Boolean
) : Parcelable