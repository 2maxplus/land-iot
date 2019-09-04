package com.hyf.iot.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppVersion(
      var versionCode: Int,
      var versionName: String,
      var isForcedUpdate: Boolean,
      var downloadUrl:String,
      var description: String? = null
): Parcelable