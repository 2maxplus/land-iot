package com.hyf.iot.domain.plan

data class IrrigatePlanGroupInfo(
    val duration: String,
    val id: String,
    val irrigatePlanGroupControlsInfos: ArrayList<IrrigatePlanGroupControlsInfo>,
    val irrigatePlanId: String,
    val name: String
)