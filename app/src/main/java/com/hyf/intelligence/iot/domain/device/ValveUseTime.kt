package com.hyf.intelligence.iot.domain.device

data class ValveUseTime(
        val date: Int,
        val useTime: List<UseTime>
)
data class UseTime(
        val startEnds: List<StartEnd>,
        val valveId: String
)

data class StartEnd(
        var end: String,
        var start: String,
        var interval:String = ""
)