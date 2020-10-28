package com.hyf.iot.domain.plan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IrrigatePlanGroupControlsInfo(
    val deviceId: String,
    val deviceNum: String, //设备编号
    val deviceName: String, //设备名称
    val deviceSignal: Int, //设备信号
    val id: String,
    val irrigatePlanGroupId: String,
    val irrigatePlanId: String,
    val sensorId: String,
    val sensorName: String,
    val deviceState: Int,  //设备状态
    val sensorState: Int, //传感器状态 4:open 2:close
    val deviceStateString: String,
    val sensorStateString: String,
    val updated: String
): Parcelable