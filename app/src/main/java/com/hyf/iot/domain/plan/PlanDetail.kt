package com.hyf.iot.domain.plan

data class PlanDetail(
    val companyId: String,
    val deviceId: String,
    val frequency: String,
    val id: String,
    val irrigatePlanGroupInfos: ArrayList<IrrigatePlanGroupInfo>,
    val name: String,
    val sensorId: String,
    val startTime: String,
    val state: Int
)