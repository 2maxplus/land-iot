package com.hyf.iot.domain.plan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlanDetail (
        val companyId: String,
        val deviceId: String,
        val frequency: String,
        val id: String,
        val irrigatePlanGroupInfos: ArrayList<IrrigatePlanGroupInfos>,
        val name: String,  //计划组名称
        val sensorId: String,
        val startTime: String, //开始时间
        val state: Int //状态
) : Parcelable