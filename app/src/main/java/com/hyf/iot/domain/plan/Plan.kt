package com.hyf.iot.domain.plan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Plan(
    val pumpRoomName: String, //泵房名称
    val farmName: String, //农场名称
    val frequencyConverterCabinetName: String, //变频柜名称
    val id: String,
    val name: String, //计划名字
    val sensorId: String,
    val startTime: String,  //开始时间
    val state: Int, //计划状态
    val stateString: String,
    val waterPumpSensorName: String //水泵名称
): Parcelable