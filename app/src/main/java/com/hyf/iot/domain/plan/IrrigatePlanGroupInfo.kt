package com.hyf.iot.domain.plan

data class IrrigatePlanGroupInfo(
        val duration: String, //持续时间
        val id: String,
        val sort: Int, // 执行顺序
        val state: Int,  //计划状态
        val irrigatePlanGroupControlsInfosRef: ArrayList<IrrigatePlanGroupControlsInfo>,
        val irrigatePlanId: String,  //计划ID
        val name: String //计划名称
)