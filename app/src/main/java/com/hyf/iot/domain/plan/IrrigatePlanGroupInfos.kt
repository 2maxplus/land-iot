package com.hyf.iot.domain.plan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IrrigatePlanGroupInfos(
        val duration: String, //持续时间
        val id: String,
        val sort: Int, // 执行顺序
        val state: Int,  //计划状态
        val openDateTime: String, //阀门打开时间
        val closeDateTime: String, //阀门关闭时间
        val irrigatePlanGroupControlsInfosRef: ArrayList<IrrigatePlanGroupControlsInfo>,
        val irrigatePlanId: String,  //计划ID
        val name: String //计划名称
): Parcelable